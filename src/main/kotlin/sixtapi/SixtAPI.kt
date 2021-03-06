package sixtapi

import io.ktor.client.call.receive
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import sixtapi.json.*
import util.Json
import util.Network
import java.util.*
import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

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

    @OptIn(InternalAPI::class)
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
        return runCatching<List<Vehicle>> { Json.jsonTranscoder.decodeFromString(string) }.onFailure { println(it) }
            .getOrElse{ return listOf() }
    }

    suspend fun getVehicle(id: String): Vehicle? {
        val string = get("/vehicles/$id")
        return runCatching<Vehicle> { Json.jsonTranscoder.decodeFromString(string) }.onFailure {
            println(it)
            println(string)
        }
            .getOrNull()
    }

    suspend fun getBookings(): List<Booking> {
        val string = get("/bookings")
        return runCatching<List<Booking>> { Json.jsonTranscoder.decodeFromString(string) }.onFailure { println(it) }.getOrElse { return listOf() }
    }

    suspend fun getBooking(id: String): Booking? {
        val string = get("/bookings/$id")
        return runCatching<Booking> { Json.jsonTranscoder.decodeFromString(string) }.getOrNull()
    }

    suspend fun postBooking(startLat: Double, startLgn: Double, dstLat: Double, dstLng: Double, id: String) {
        post("/bookings", false, Booking(dstLat, dstLng, startLat, startLgn, id, null, BookingStatus.CREATED))
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

    /**!!!MOCK!!!*/
    @OptIn(ExperimentalTime::class)
    suspend fun getPersons(): List<Person> {
        val bookings = getBookings()
        val rand = Random(123)
        return bookings.map {
            Person(
                timeBuffer = 10.minutes + rand.nextInt(50).minutes,
                rand.nextInt(5),
                it.bookingID
            )
        }
    }

    suspend fun getPersonOfBooking(bID: String): Person? {
        return getPersons().find { it.bookingID == bID }
    }
}