package gui

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.content.default
import io.ktor.http.content.defaultResource
import io.ktor.http.content.file
import io.ktor.http.content.files
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.response.respondFile
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.io.File

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
    routing {
        static {
            resource("/", "index.html")
            resources("css")
            resources("js")
        }

    }
}
