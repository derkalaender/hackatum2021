package sixtapi

import io.ktor.client.request.*
import io.ktor.client.statement.*
import sixtapi.json.Vehicle
import util.Network

object SixtAPI {
    const val endpoint = "https://us-central1-sixt-hackatum-2021-black.cloudfunctions.net/api"

    suspend fun getVehicles() : List<Vehicle> {
        val client = Network.createClient()

        val response = kotlin.runCatching { client.get<HttpResponse>("$endpoint/vehicles")}.getOrElse {  }
        //val string = response.
    }
}