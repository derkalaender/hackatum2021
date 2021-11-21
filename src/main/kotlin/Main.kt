import googleapi.GoogleAPI
import googleapi.json.Geocode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import routefinding.RouteFinder
import sixtapi.SixtAPI
import server.RobotaxiGui
import sixtapi.json.VehicleStatus
import util.Json
import kotlin.math.sqrt

suspend fun main () {
    //RobotaxiGui.start()

    //val test = RouteFinder.findRoutes(48.072023, 11.522836, 48.068866, 11.523638)
    //println(test)
    test()
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