package routefinding

import data.Location
import googleapi.GoogleAPI
import googleapi.json.Directions
import googleapi.json.DirectionsLeg
import googleapi.json.DirectionsRoute
import googleapi.json.DirectionsStep
import kotlinx.serialization.Serializable
import sixtapi.SixtAPI
import sixtapi.json.Booking
import sixtapi.json.VehicleStatus
import kotlin.math.abs
import kotlin.math.sqrt

@Serializable
data class RouteResult(
    val id: String,
    val mergedID: String,
    val standardGeometry: RouteGeometry,
    val standardMeta: RouteMeta,
    val mergedGeometry: RouteGeometry,
    val mergedMeta: RouteMeta,
    val oldPolyline : String,
)

@Serializable
data class RouteGeometry(
    val path: List<Location>,
    val polyline: String
)

@Serializable
data class RouteMeta(
    val CO2: Double,
    val time: Int,
    val distance: Int,
)

@Serializable
class RouteSearchResult(
    val standardDirection: Directions?,
    val mergedDirection: Directions?,
    val booking: Booking?,
    val oldDirection: Directions?
) {
    companion object {
        fun calcCO2(kwH: Double): Double {
            return 0.366 /*kg*/ * kwH
        }

        fun calcKWH(dist: Int): Double {
            return dist * 1.724137931034483e-4
        }
    }

    val standardDistance: Int = standardDirection?.let { RouteFinder.calculateDistance(it) } ?: -1
    val mergedDistance: Int = mergedDirection?.let {
        if(standardDirection != null) {
            val shortest = RouteFinder.getShortestRoute(standardDirection)
            RouteFinder.getDistanceIn(shortest.legs.first().end_location, shortest.legs.last().end_location, it)
        } else null
    } ?: -1
    val standardCO2 = calcCO2(calcKWH(standardDistance))
    val mergedCO2 = calcCO2(calcKWH(mergedDistance))
    val standardTime = standardDirection?.let { RouteFinder.calculateTime(it) } ?: -1
    val mergedTime = mergedDirection?.let {
        if (standardDirection != null){
            val shortest = RouteFinder.getShortestRoute(standardDirection)
            RouteFinder.getTimeIn(shortest.legs.first().end_location, shortest.legs.last().end_location, it)
        } else null
    } ?: -1
    val standardRoute: DirectionsRoute? = standardDirection?.let { RouteFinder.getShortestRoute(it) }
    val mergedRoute: DirectionsRoute? = mergedDirection?.let {
        if(mergedDistance > standardDistance) null else
        RouteFinder.getShortestRoute(it)
    }
}

object RouteFinder {

    suspend fun findRoutes(
        srcLat: Double,
        srcLng: Double,
        dstLat: Double,
        dstLng: Double
    ): RouteSearchResult {
        val (freeCars, _) = SixtAPI.getVehicles().partition { it.status == VehicleStatus.FREE }
        val closeCars =
            freeCars.sortedBy { sqrt((it.lat - srcLat) * (it.lat - srcLat) + (it.lng - srcLng) * (it.lng - srcLng)) }
                .take(5)
        val freeCarMatrix = GoogleAPI.getMatrix(
            origins = listOf(Pair(srcLat.toString(), srcLng.toString())),
            destinations = closeCars.map { Pair(it.lat.toString(), it.lng.toString()) }
        )!! //TODO error

        val closestCar =
            closeCars.mapIndexed { index, it -> Pair(freeCarMatrix.rows.first().elements[index].duration.value, it) }
                .minByOrNull { it.first }
        val directionSrcToDst = closestCar?.let {
            GoogleAPI.getDirections(
                it.second.lat,
                it.second.lng,
                dstLat,
                dstLng,
                listOf(Pair(srcLat, srcLng))
            )
        }

        val bookings = SixtAPI.getBookings()
        //TODO check error
        val shortestPickupDirection = bookings.mapNotNull { booking ->
            val vID = booking.vehicleID
            val vehicle = vID?.let { SixtAPI.getVehicle(it) }
            if(vehicle != null) {
                val firstDestMatrix = GoogleAPI.getMatrix(
                    origins = listOf(Pair(vehicle.lat.toString(), vehicle.lng.toString())),
                    destinations = listOf(
                        Pair(booking.destinationLat.toString(), booking.destinationLng.toString()),
                        Pair(srcLat.toString(), srcLng.toString()),
                    )
                )!!
                val distBookingDst = firstDestMatrix.rows.first().elements[0].distance.value
                val distNewSrc = firstDestMatrix.rows.first().elements[1].distance.value

                val points = mutableListOf<Location>()
                points.add(Location(vehicle.lat, vehicle.lng))
                if(distBookingDst < distNewSrc) points.add(Location(booking.destinationLat, booking.destinationLng))
                else points.add(Location(srcLat, srcLng))

                if(points.last() == Location(booking.destinationLat, booking.destinationLng)) {
                    points.add(Location(srcLat, srcLng))
                    points.add(Location(dstLat, dstLng))
                } else { //picked up
                    val secondDestMatrix = GoogleAPI.getMatrix(
                        origins = listOf(points.last().let { Pair(it.lat.toString(), it.lng.toString()) }) ,
                        destinations = listOf(
                            Pair(booking.destinationLat.toString(), booking.destinationLng.toString()),
                            Pair(dstLat.toString(), dstLng.toString()),
                        )
                    )!!

                    val distBookingDst2 = secondDestMatrix.rows.first().elements[0].distance.value
                    val distNewDst = secondDestMatrix.rows.first().elements[1].distance.value

                    if(distBookingDst2 < distNewDst) {
                        points.add(Location(booking.destinationLat, booking.destinationLng))
                        points.add(Location(dstLat, dstLng))
                    } else {
                        points.add(Location(dstLat, dstLng))
                        points.add(Location(booking.destinationLat, booking.destinationLng))
                    }
                }

                val direction = GoogleAPI.getDirections(
                    startLat = points[0].lat,
                    startLng = points[0].lng,
                    dstLat = points[3].lat,
                    dstLng = points[3].lng,
                    waypoints = points.subList(1, 3).map { Pair(it.lat, it.lng) }
                )!!
                //TODO check error

                val oldDirections = GoogleAPI.getDirections(
                    startLat = vehicle.lat,
                    startLng = vehicle.lng,
                    dstLat = booking.destinationLat,
                    dstLng = booking.destinationLng
                )
                Triple(direction, booking, oldDirections)
            } else null
        }

        val shortestDirection = kotlin.runCatching {
            shortestPickupDirection
                .filter { (newDirections, booking,_) ->
                    val person = SixtAPI.getPersonOfBooking(booking.bookingID) ?: return@filter false
                    val oldDirections = GoogleAPI.getDirections(
                        booking.pickupLat,
                        booking.pickupLng,
                        booking.destinationLat,
                        booking.destinationLng
                    ) ?: return@filter false
                    calculateTime(oldDirections) + person.timeBuffer.inWholeSeconds >= calculateTime(newDirections)
                }
                .minByOrNull { (directions, _) ->
                    getShortestRoute(directions).legs.sumOf { it.distance.value }
                }
        }.getOrNull()

        return RouteSearchResult(directionSrcToDst, shortestDirection?.first, shortestDirection?.second, shortestDirection?.third)
    }

    fun calculateDistance(directions: Directions): Int {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.distance.value } }!!.legs.sumOf { it.distance.value }
    }

    fun calculateTime(directions: Directions): Int {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.duration.value } }!!.legs.sumOf { it.duration.value }
    }

    fun getShortestRoute(directions: Directions): DirectionsRoute {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.duration.value } }!!
    }

    private fun subLegsIn(start: Location, dest: Location, directions: Directions): List<DirectionsLeg> {
        return directions.routes.minByOrNull { route -> route.legs.sumOf { it.duration.value } }!!
            .legs.dropWhile { it.start_location != start }.dropLastWhile { it.end_location != dest }

    }

    fun getDistanceIn(start: Location, dest: Location, directions: Directions): Int {
        return subLegsIn(start, dest, directions)
            .sumOf { it.distance.value }
    }

    fun getTimeIn(start: Location, dest: Location, directions: Directions): Int {
        return subLegsIn(start, dest, directions)
            .sumOf { it.duration.value }
    }
}