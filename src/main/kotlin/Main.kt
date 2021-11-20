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

    val res = RouteFinder.findRoutes(48.191083, 11.619449, 48.1090993, 11.5004064)
}