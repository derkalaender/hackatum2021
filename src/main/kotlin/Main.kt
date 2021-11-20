import kotlinx.coroutines.runBlocking
import sixtapi.SixtAPI
import server.RobotaxiGui

suspend fun main () {
    //RobotaxiGui.start()
    SixtAPI.getVehicles()
}