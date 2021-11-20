package googleapi

import googleapi.json.*
import io.ktor.client.call.receive
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.decodeFromString
import util.DirectionsOptions
import util.Json
import util.Network
import java.net.URLEncoder

object GoogleAPI {
    private val directionsCache = mutableMapOf<DirectionsOptions, Directions>()

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
        }.onFailure { println(it) }
            .getOrElse { return null }
        val string = result.receive<String>()
        return kotlin.runCatching<GeocodeJson> { Json.jsonTranscoder.decodeFromString(string) }.onFailure { println(it) }.getOrNull()
            ?.let { Geocode(it.results, GeocodeStatus.stringToStatus(it.status)) }
    }

    suspend fun getDirections(startLat: Double, startLng: Double, dstLat: Double, dstLng: Double, waypoints: List<Pair<Double, Double>>? = null): Directions? {
        val directionsOptions = DirectionsOptions(startLat, startLng, dstLat, dstLng)
        directionsCache[directionsOptions]?.let { return it }

        val client = Network.createClient()

        val result = kotlin.runCatching<HttpResponse> {
            client.get(
                "$endpoint/directions/json?" +
                    "origin=${URLEncoder.encode("$startLat,$startLng", Charsets.UTF_8)}" +
                    "&destination=${URLEncoder.encode("$dstLat,$dstLng", Charsets.UTF_8)}" +
                        if (waypoints != null) "&waypoints=${URLEncoder.encode(waypoints.joinToString(separator = "|") { "${it.first},${it.second}" }, Charsets.UTF_8)}" else "" +
                    "&key=$key"
            )
        }.onFailure { println(it) }.getOrElse { return null }
        val string = result.receive<String>()
        return kotlin.runCatching<Directions> { Json.jsonTranscoder.decodeFromString(string) }
            .onFailure { println(it) }.getOrNull()
            ?.also { directionsCache[directionsOptions] = it }
    }

    suspend fun getMatrix(origins: List<Pair<String, String>>, destinations: List<Pair<String,String>>) : Matrix? {
        val client = Network.createClient()

        val result = kotlin.runCatching<HttpResponse> {
            client.get(
                "$endpoint/distancematrix/json?" +
                        "destinations=${URLEncoder.encode(destinations.joinToString(separator = "|") { "${it.first},${it.second}" }, Charsets.UTF_8)}" +
                        "&origins=${URLEncoder.encode(origins.joinToString(separator = "|") { "${it.first},${it.second}" }, Charsets.UTF_8)}" +
                        "&key=$key"
            )
        }.onFailure { println(it) }.getOrElse { return null }
        val string = result.receive<String>()
        return kotlin.runCatching<MatrixJson> { Json.jsonTranscoder.decodeFromString(string) }.onFailure { println(it) }.getOrNull()
            ?.let { Matrix(it.destination_addresses, it.origin_addresses, it.rows, MatrixStatus.stringToStatus(it.status)) }
    }
}