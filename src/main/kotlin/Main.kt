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

    val origin0 = GoogleAPI.addressToGeocode("Freisingerlandstraße 19 München 80939") ?: return
    val origin1 = GoogleAPI.addressToGeocode("Carl-Fohr-Straße 17 Miesbach 83714")?: return
    val origin2 = GoogleAPI.addressToGeocode("Therese-Giehse-Allee 59, 81739 München")?: return
    val dest = GoogleAPI.addressToGeocode("Margaretenstraße 59, 82152 Krailling")?: return

    fun conv(geo: Geocode) : Pair<String, String> {
        return geo.results.first().geometry.location.let { Pair(it.lat.toString(), it.lng.toString()) }
    }

    val origins = listOf(conv(origin0),conv(origin2),conv(origin2))
    val destinations = listOf(conv(dest))

    val res = GoogleAPI.getMatrix(origins, destinations) ?: return
    println(Json.jsonTranscoder.encodeToString(res.toJson()))

}