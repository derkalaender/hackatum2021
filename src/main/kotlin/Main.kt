
import gui.RobotaxiGui
import kotlinx.coroutines.runBlocking
import sixtapi.SixtAPI

suspend fun main () {
    //RobotaxiGui.start()
    SixtAPI.getVehicles()
}