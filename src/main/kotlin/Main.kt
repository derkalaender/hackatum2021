import googleapi.GoogleAPI
import googleapi.json.Geocode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import routefinding.RouteFinder
import sixtapi.SixtAPI
import server.RobotaxiGui
import util.Json

suspend fun main () {
    //RobotaxiGui.start()

    val test = RouteFinder.findRoutes(48.072023, 11.522836, 48.068866, 11.523638)
    println(test)
}