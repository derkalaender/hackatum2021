package sixtapi

import io.ktor.client.call.receive
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.decodeFromString
import sixtapi.json.*
import util.Json
import util.Network

object SixtAPI {
    const val endpoint = "https://us-central1-sixt-hackatum-2021-black.cloudfunctions.net/api"

    private suspend fun get(item: String): String {
        val client = Network.createClient()

        val response =
            kotlin.runCatching { client.get<HttpResponse>("$endpoint$item") }
                .getOrElse {
                    client.close()
                    return ""
                }
        val string = response.receive<String>()
        client.close()
        return string
    }

    private suspend fun post(item: String, await: Boolean, json: Any? = null): String {
        val client = Network.createClient()

        val response =
            kotlin.runCatching {
                client.post<HttpResponse>("$endpoint$item") {
                    if (json != null) {
                        headers {
                            append("content-type", "application/json")
                        }
                        body = json
                    }
                }
            }
                .getOrElse {
                    client.close()
                    return ""
                }

        if (await) {
            val string = response.receive<String>()
            client.close()
            return string
        }
        client.close()
        return ""
    }

    private suspend fun delete(item: String, await: Boolean): String {
        val client = Network.createClient()

        val response =
            kotlin.runCatching { client.delete<HttpResponse>("$endpoint$item") }
                .getOrElse {
                    client.close()
                    return ""
                }

        if (await) {
            val string = response.receive<String>()
            client.close()
            return string
        }
        client.close()
        return ""
    }

    suspend fun getVehicles(): List<Vehicle> {
        val string = get("/vehicles")
        return runCatching<List<VehicleJson>> { Json.jsonTranscoder.decodeFromString(string) }.getOrElse { return listOf() }
            .map { Vehicle(it.charge, it.lat, it.lng, VehicleStatus.stringToStatus(it.status), it.vehicleID) }
    }

    suspend fun getVehicle(id: String): Vehicle? {
        val string = get("/vehicle/$id")
        return runCatching<VehicleJson> { Json.jsonTranscoder.decodeFromString(string) }.getOrNull()
            ?.let { Vehicle(it.charge, it.lat, it.lng, VehicleStatus.stringToStatus(it.status), it.vehicleID) }
    }

    suspend fun getBookings(): List<Booking> {
        val string = get("/bookings")
        return runCatching<List<Booking>> { Json.jsonTranscoder.decodeFromString(string) }.onFailure {
            println(it)
        }.getOrElse { return listOf() }
    }

    suspend fun getBooking(id: String): Booking? {
        val string = get("/bookings/$id")
        return runCatching<Booking> { Json.jsonTranscoder.decodeFromString(string) }.getOrNull()
    }

    suspend fun postBooking(startLat: Double, startLgn: Double, dstLat: Double, dstLng: Double) {
        // not needed
        // post("/bookings", false, Booking(dstLat, dstLng, startLat, startLgn))
    }

    suspend fun deleteBooking(id: String) {
        delete("/bookings/$id", false)
    }

    suspend fun assignVehicleToBook(bID: String, vID: String) {
        post("/bookings/$bID/assignVehicle/$vID", false)
    }

    suspend fun passGotOn(bID: String) {
        post("/bookings/$bID/passengerGotOn", false)
    }

    suspend fun passGotOff(bID: String) {
        post("/bookings/$bID/passengerGotOff", false)
    }

    suspend fun getPersons(): List<Person> {
        return emptyList()
    }

    suspend fun getPersonOfBooking(bID: String): Person? {
        return getPersons().find { it.bookingID == bID }
    }
}