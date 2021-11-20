package routefinding

import data.Location
import googleapi.GoogleAPI
import googleapi.json.Directions
import sixtapi.SixtAPI

object RouteFinder {

    suspend fun findRoutes(srcLat: Double, srcLng: Double, dstLat: Double, dstLng: Double) : Pair<Directions, Directions> {
        val directionSrcToDst = GoogleAPI.getDirections(srcLat, srcLng, dstLat, dstLng)!!
        //TODO use this information

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
            direction
        }
            .minByOrNull { directions ->
                directions.routes.minByOrNull { route ->
                    route.legs.sumOf { it.distance.value } }!!
                    .legs.sumOf { it.distance.value }
            }!!


        return Pair(directionSrcToDst, shortestPickupDirection)
    }
}