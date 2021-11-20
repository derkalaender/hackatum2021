package googleapi.json

import data.Location
import kotlinx.serialization.Serializable
import kotlin.io.path.createTempDirectory

enum class DirectionsStatus {
    OK, NOT_FOUND, ZERO_RESULTS, MAX_WAYPOINTS_EXCEEDED,MAX_ROUTE_LENGTH_EXCEEDED,INVALID_REQUEST,OVER_DAILY_LIMIT,OVER_QUERY_LIMIT,REQUEST_DENIED,UNKNOWN_ERROR;

    companion object {
        fun stringToStatus(string: String) : DirectionsStatus {
            return values().find { it.name == string } ?: UNKNOWN_ERROR
        }
    }
}

data class Directions(
    val geocoded_waypoints: List<DirectionsGeocodedWaypoint>,
    val routes: List<DirectionsRoute>,
    val status: DirectionsStatus
) {
    fun toJson() : DirectionsJson {
        return DirectionsJson(
            geocoded_waypoints,
            routes,
            status.name
        )
    }
}

@Serializable
data class DirectionsJson(
    val geocoded_waypoints: List<DirectionsGeocodedWaypoint> = listOf(),
    val routes: List<DirectionsRoute> = listOf(),
    val status: String = ""
)

@Serializable
data class DirectionsGeocodedWaypoint(
    val geocoder_status: String = "",
    val place_id: String = "",
    val types: List<String> = listOf()
)

@Serializable
data class DirectionsRoute(
    val bounds: DirectionsBounds = DirectionsBounds(),
    val copyrights: String = "",
    val legs: List<DirectionsLeg> = listOf(),
    val overview_polyline: DirectionsPolyline = DirectionsPolyline(),
    val summary: String = "",
    val waypoint_order : List<Int> = listOf()
)

@Serializable
data class DirectionsBounds(
    val northeast: Location = Location(),
    val southwest: Location = Location()
)

@Serializable
data class DirectionsLeg(
    val distance: DirectionsIntEntry = DirectionsIntEntry(),
    val duration: DirectionsIntEntry = DirectionsIntEntry(),
    val end_address: String = "",
    val end_location: Location = Location(),
    val start_address: String = "",
    val start_location: Location = Location(),
    val steps: List<DirectionsStep> = listOf(),
)

@Serializable
data class DirectionsStep(
    val distance: DirectionsIntEntry = DirectionsIntEntry(),
    val duration: DirectionsIntEntry = DirectionsIntEntry(),
    val end_location: Location = Location(),
    val html_instructions: String = "",
    val maneuver: String = "",
    val polyline: DirectionsPolyline = DirectionsPolyline(),
    val start_location: Location = Location(),
    val travel_mode: String = ""
)

@Serializable
data class DirectionsIntEntry(
    val text: String = "",
    val value: Int = 0
)

@Serializable
data class DirectionsPolyline(
    val points: String = ""
)