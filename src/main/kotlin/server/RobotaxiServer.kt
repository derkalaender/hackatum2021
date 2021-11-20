package server

import googleapi.GoogleAPI
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.json
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import routefinding.RouteFinder
import sixtapi.SixtAPI
import util.DirectionsOptions

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

        post("/directions") {
            // TODO proper error handling
            val query = call.receive<Query>()
            val directions = GoogleAPI.getDirections(query.start.lat, query.start.lng, query.destination.lat, query.destination.lng)
            val route = directions?.routes?.minByOrNull { r ->
                r.legs.sumOf { it.duration.value }
            } ?: run {
                call.respond(
                    HttpStatusCode.BadRequest
                )
                return@post
            }

            call.respond(route)
        }

        get("/bookings") {
            call.respond(SixtAPI.getBookings())
        }

        post("/route") {
            val query = call.receive<Query>()
            val routes = RouteFinder.findRoutes(query.start.lat, query.start.lng, query.destination.lat, query.destination.lng)
            call.respond(routes)
        }
    }
}
