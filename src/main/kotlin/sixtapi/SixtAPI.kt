package sixtapi

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.decodeFromString
import sixtapi.json.Booking
import sixtapi.json.GetVehicles
import sixtapi.json.Vehicle
import util.Json
import util.Network

object SixtAPI {
    const val endpoint = "https://us-central1-sixt-hackatum-2021-black.cloudfunctions.net/api"

    private suspend fun get(item: String) : String {
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

    private suspend fun post(item: String, await: Boolean, json: Any? = null) : String {
        val client = Network.createClient()

        val response =
            kotlin.runCatching {
                client.post<HttpResponse>("$endpoint$item") {
                    if(json != null) {
                        headers {
                            append("content-type", "application/json")
                        }
                        body=json
                    }
                }
            }
                .getOrElse {
                    client.close()
                    return ""
                }

        if(await) {
            val string = response.receive<String>()
            client.close()
            return string
        }
        client.close()
        return ""
    }

    private suspend fun delete(item: String, await: Boolean) : String {
        val client = Network.createClient()

        val response =
            kotlin.runCatching { client.delete<HttpResponse>("$endpoint$item") }
                .getOrElse {
                    client.close()
                    return ""
                }

        if(await) {
            val string = response.receive<String>()
            client.close()
            return string
        }
        client.close()
        return ""
    }

    suspend fun getVehicles(): List<Vehicle> {
        val string = get("/vehicles")
        return runCatching<GetVehicles> { Json.jsonTranscoder.decodeFromString(string) }.getOrElse { return listOf() }
    }

    suspend fun getVehicle(id: String) : Vehicle? {
        val string = get("/vehicle/$id")
        return runCatching<Vehicle> { Json.jsonTranscoder.decodeFromString(string) }.getOrNull()
    }

    suspend fun postBooking(startLat: Double, startLgn: Double, dstLat: Double, dstLng: Double) {
        post("/bookings", false, Booking(dstLat, dstLng, startLat, startLgn))
    }

    suspend fun deleteBooking(id: String) {
        delete("/bookings/$id", false)
    }

    suspend fun assignVehiToBook(bID: String, vID: String) {
        post("/bookings/$bID/assignVehicle/$vID", false)
    }

    suspend fun passGotOn(bID: String) {
        post("/bookings/$bID/passengerGotOn", false)
    }

    suspend fun passGotOff(bID: String) {
        post("/bookings/$bID/passengerGotOff", false)
    }
}