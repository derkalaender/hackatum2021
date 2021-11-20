package googleapi.json

import data.Location
import kotlinx.serialization.Serializable

enum class GeocodeStatus {
    OK, ZERO_RESULTS, OVER_DAILY_LIMIT, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST, UNKNOWN_ERROR;

    companion object {
        fun stringToStatus(string: String) : GeocodeStatus {
            return GeocodeStatus.values().find { it.name == string } ?: UNKNOWN_ERROR
        }
    }
}

data class Geocode(
    val results: List<GeocodeResult> = listOf(),
    val status: GeocodeStatus = GeocodeStatus.UNKNOWN_ERROR
) {
    fun toJson() : GeocodeJson {
        return GeocodeJson(
            results,
            status.name
        )
    }
}

@Serializable
data class GeocodeJson(
    val results: List<GeocodeResult> = listOf(),
    val status: String = ""
)

@Serializable
data class GeocodeResult(
    val address_components: List<GeocodeAddressComponent> = listOf(),
    val formatted_address: String = "",
    val geometry: GeocodeGeometry = GeocodeGeometry(),
    val place_id: String = "",
    val types: List<String> = listOf()
)

@Serializable
data class GeocodeAddressComponent(
    val long_name: String = "",
    val short_name: String = "",
    val types: List<String> = listOf()
)

@Serializable
data class GeocodeGeometry(
    val location: Location = Location(),
    val location_type: String = "",
    val viewport: GeocodeViewport = GeocodeViewport()
)

@Serializable
data class GeocodeViewport(
    val northeast: Location = Location(),
    val southwest: Location = Location()
)