import googleapi.GoogleAPI
import googleapi.json.Geocode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import sixtapi.SixtAPI
import server.RobotaxiGui
import util.Json

suspend fun main () {
    RobotaxiGui.start()
}