package googleapi

import googleapi.json.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import util.Json
import util.Network
import java.net.URLEncoder

object GoogleAPI {

    const val key = "AIzaSyBXqG0fwm1zQ_XlPujzFV3OXbGkggCUBnA"
    const val endpoint = "https://maps.googleapis.com/maps/api"

    suspend fun addressToGeocode(address: String): Geocode? {
        val client = Network.createClient()

        val result = kotlin.runCatching<HttpResponse> {
            client.get(
                "$endpoint/geocode/json?address=${
                    URLEncoder.encode(
                        address,
                        Charsets.UTF_8
                    )
                }&key=$key"
            )
        }
            .getOrElse { return null }
        val string = result.receive<String>()
        return kotlin.runCatching<GeocodeJson> { Json.jsonTranscoder.decodeFromString(string) }.getOrNull()
            ?.let { Geocode(it.results, GeocodeStatus.stringToStatus(it.status)) }
    }

    suspend fun getDirections(startLat: Double, startLng: Double, dstLat: Double, dstLng: Double): Directions? {
        val client = Network.createClient()

        val result = kotlin.runCatching<HttpResponse> {
            client.get(
                "$endpoint/directions/json?" +
                        "origin=${URLEncoder.encode("$startLat,$startLng", Charsets.UTF_8)}" +
                        "&destination=${URLEncoder.encode("$dstLat,$dstLng", Charsets.UTF_8)}" +
                        "&key=$key"
            )
        }.getOrElse { return null }
        val string = result.receive<String>()
        return kotlin.runCatching<DirectionsJson> { Json.jsonTranscoder.decodeFromString(string) }.onFailure { println(it) }.getOrNull()
            ?.let { Directions(it.geocoded_waypoints, it.routes, DirectionsStatus.stringToStatus(it.status)) }
    }
}