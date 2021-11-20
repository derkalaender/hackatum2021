import googleapi.GoogleAPI
import googleapi.json.Geocode
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import sixtapi.SixtAPI
import server.RobotaxiGui
import util.Json

suspend fun main () {
    //RobotaxiGui.start()
    //SixtAPI.getVehicles()

    val origin0 = GoogleAPI.addressToGeocode("Waldheimpl. 58, 81739 München")
    val origin1 = GoogleAPI.addressToGeocode("Fürstenrieder Str. 288, 81377 München")
    val origin2 = GoogleAPI.addressToGeocode("Arcisstraße 21, 80333 München")
    val origin3 = GoogleAPI.addressToGeocode("Otto-Hahn-Ring 6/Gebäude 48, 81739 München")
    val origin4 = GoogleAPI.addressToGeocode("Kieferngartenstraße 5, 80939 München")
    val origin5 = GoogleAPI.addressToGeocode("Münchener Str. 1, 82110 Germering")

    listOf(origin0, origin1, origin2, origin3, origin4, origin5).filterNotNull().map { it.results.first().geometry.location }.forEach { println(it) }
}