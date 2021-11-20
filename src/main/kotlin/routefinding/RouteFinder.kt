package routefinding

import data.Location
import googleapi.GoogleAPI
import googleapi.json.Directions
import googleapi.json.DirectionsRoute
import kotlinx.serialization.Serializable
import sixtapi.SixtAPI
import sixtapi.json.Booking
import sixtapi.json.VehicleStatus

@Serializable
class RouteSearchResult(
    val standardDirection : Directions?,
    val mergedDirection : Directions?,
    val booking: Booking?
) {
    companion object {
        fun calcCO2(kwH: Double) : Double {
            return 0.366 /*kg*/ * kwH
        }

        fun calcKWH(dist: Int) : Double {
            return dist * 1.724137931034483e-4
        }
    }

    val standardDistance : Int = standardDirection?.let { RouteFinder.calculateDistance(it) } ?: -1
    val mergedDistance : Int = mergedDirection?.let { RouteFinder.calculateDistance(it) } ?: -1
    val standardCO2 = calcCO2(calcKWH(standardDistance))
    val mergedCO2 = calcCO2(calcKWH(mergedDistance))
    val standardTime = standardDirection?.let { RouteFinder.calculateTime(it) } ?: -1
    val mergedTime = mergedDirection?.let { RouteFinder.calculateTime(it) } ?: -1
    val standardRoute : DirectionsRoute? =  standardDirection?.let { RouteFinder.getShortestRoute(it) }
    val mergedRoute : DirectionsRoute? = mergedDirection?.let { RouteFinder.getShortestRoute(it) }

}

object RouteFinder {

    suspend fun findRoutes(
        srcLat: Double,
        srcLng: Double,
        dstLat: Double,
        dstLng: Double
    ): RouteSearchResult {
        val (freeCars, _) = SixtAPI.getVehicles().partition { it.status == VehicleStatus.FREE }
        val freeCarMatrix = GoogleAPI.getMatrix(
            origins = listOf(Pair(srcLat.toString(), srcLng.toString())),
            destinations = freeCars.map { Pair(it.lat.toString(), it.lng.toString()) }
        )!! //TODO error
        val closestCar = freeCars.mapIndexed { index, it -> Pair(freeCarMatrix.rows.first().elements[index].distance.value, it) }.minByOrNull { it.first }
        val directionSrcToDst = closestCar?.let { GoogleAPI.getDirections(it.second.lat, it.second.lng, dstLat, dstLng, listOf(Pair(srcLat, srcLng))) }

        val bookings = SixtAPI.getBookings()
        //TODO check error
        val shortestPickupDirection = bookings.map { booking ->
            val bookingSrcMatrix = GoogleAPI.getMatrix(
                origins = listOf(Pair(booking.pickupLat.toString(), booking.pickupLng.toString())),
                destinations = listOf(
                    Pair(booking.destinationLat.toString(), booking.destinationLng.toString()),
                    Pair(srcLat.toString(), srcLng.toString()),
                    Pair(dstLat.toString(), dstLng.toString()),
                )
            )!!
            //TODO check error
            val distBookingDst = bookingSrcMatrix.rows.first().elements[0]
            val distNewSrc = bookingSrcMatrix.rows.first().elements[1]
            val distNewDst = bookingSrcMatrix.rows.first().elements[2]

            val travelPoints = listOf(
                Pair(distBookingDst, Location(booking.destinationLat, booking.destinationLng)),
                Pair(distNewSrc, Location(srcLat, srcLng)),
                Pair(distNewDst, Location(dstLat, dstLng))
            ).sortedBy { it.first.distance.value }

            val direction = GoogleAPI.getDirections(
                startLat = booking.pickupLat,
                startLng = booking.pickupLng,
                dstLat = travelPoints[2].second.lat,
                dstLng = travelPoints[2].second.lng,
                waypoints = travelPoints.subList(0, 2).map { Pair(it.second.lat, it.second.lng) }
            )!!
            //TODO check error
            Pair(direction, booking)
        }
        val shortestDirection = kotlin.runCatching {
            shortestPickupDirection
                .minByOrNull { (directions, _) ->
                    directions.routes.minByOrNull { route ->
                        route.legs.sumOf { it.distance.value }
                    }!!
                        .legs.sumOf { it.distance.value }
                }
        }.getOrNull()

        return RouteSearchResult(directionSrcToDst, shortestDirection?.first, shortestDirection?.second)
    }

    fun calculateDistance(directions: Directions) : Int {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.distance.value } }!!.legs.sumOf { it.distance.value }
    }

    fun calculateTime(directions: Directions) : Int {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.duration.value } }!!.legs.sumOf { it.duration.value }
    }

    fun getShortestRoute(directions: Directions) : DirectionsRoute {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.distance.value } }!!
    }
}