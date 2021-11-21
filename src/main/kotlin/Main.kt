import googleapi.GoogleAPI
import googleapi.json.Geocode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import routefinding.RouteFinder
import routefinding.RouteGeometry
import routefinding.RouteMeta
import routefinding.RouteResult
import sixtapi.SixtAPI
import server.RobotaxiGui
import sixtapi.json.VehicleStatus
import util.Json
import kotlin.math.sqrt

suspend fun main () {
    RobotaxiGui.start()

//    val routes = RouteFinder.findRoutes(48.196188, 11.578347, 48.068866, 11.523638)
//
//    val routeResult = RouteResult(
//        id = "",
//        mergedID = routes.booking?.bookingID ?: "---",
//        standardGeometry = RouteGeometry(
//            path = routes.standardRoute?.legs?.map { listOf(it.start_location, it.end_location) }?.flatten() ?: listOf(),
//            polyline = routes.standardRoute?.overview_polyline?.points ?: ""
//        ),
//        standardMeta = RouteMeta(
//            CO2 = routes.standardCO2,
//            time = routes.standardTime,
//            distance = routes.standardDistance
//        ),
//        mergedGeometry = RouteGeometry(
//            path = routes.mergedRoute?.legs?.map { listOf(it.start_location, it.end_location) }?.flatten() ?: listOf(),
//            polyline = routes.mergedDirection?.let { RouteFinder.getShortestRoute(it).overview_polyline.points
//            } ?: ""
//        ),
//        mergedMeta = RouteMeta(
//            CO2 = routes.mergedCO2,
//            time = routes.mergedTime,
//            distance = routes.mergedDistance
//        ),
//        oldPolyline = routes.oldDirection?.let { RouteFinder.getShortestRoute(it) }?.overview_polyline?.points ?: ""
//    )

    //println(routeResult)
    //test()
}

suspend fun test() {
    val bookings = SixtAPI.getBookings()

    SixtAPI.getVehicles().forEach { println(it) }

    bookings.forEach { booking ->
        val (freeCars, _) = SixtAPI.getVehicles().partition { it.status == VehicleStatus.FREE }
        val closeCars =
            freeCars.sortedBy { sqrt((it.lat - booking.pickupLat) * (it.lat - booking.pickupLat) + (it.lng -
                    booking.pickupLng) * (it.lng - booking.pickupLng)) }
                .take(5)
        val freeCarMatrix = GoogleAPI.getMatrix(
            origins = listOf(Pair(booking.pickupLat.toString(), booking.pickupLng.toString())),
            destinations = closeCars.map { Pair(it.lat.toString(), it.lng.toString()) }
        )!! //TODO error

        val closestCar =
            closeCars.mapIndexed { index, it -> Pair(freeCarMatrix.rows.first().elements[index].duration.value, it) }
                .minByOrNull { it.first }?.second

        closestCar?.let {
            SixtAPI.assignVehicleToBook(booking.bookingID, it.vehicleID)
        }

        if (closestCar == null) println("No car for bID: ${booking.bookingID}")

    }

}