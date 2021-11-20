package server

import googleapi.GoogleAPI
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import sixtapi.SixtAPI

object RobotaxiGui {
    private lateinit var server: ApplicationEngine

    /**
     * Start the Robotaxi server for the GUI
     */
    fun start() {
        val env = applicationEngineEnvironment {
            connector {
                port = 8080
            }
            module(Application::module)
        }

        server = embeddedServer(Netty, env)
        server.start()
    }

    fun stop() {
        server.stop(5000, 5000)
    }
}

private fun Application.module() {
    install(CORS) {
        anyHost()
        method(HttpMethod.Post)
        method(HttpMethod.Get)
        method(HttpMethod.Options)
        header(HttpHeaders.ContentType)
    }

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/vehicles") {
            call.respond(SixtAPI.getVehicles())
        }

        get("/directions") {
            // TODO proper error handling
            val lngSrc = call.request.queryParameters["lngSrc"]!!.toDouble()
            val latSrc = call.request.queryParameters["latSrc"]!!.toDouble()
            val lngDst = call.request.queryParameters["lngDst"]!!.toDouble()
            val latDst = call.request.queryParameters["latDst"]!!.toDouble()

            val route = GoogleAPI.getDirections(lngSrc, latSrc, lngDst, latDst)!!.routes.minByOrNull { r ->
                r.legs.sumOf { it.duration.value }
            }!!

            call.respond(route)
        }

        get("/bookings") {
            call.respond(SixtAPI.getBookings())
        }
    }
}
