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
    val results: List<GeocodeResult>,
    val status: GeocodeStatus
)

@Serializable
data class GeocodeJson(
    val results: List<GeocodeResult>,
    val status: String
)

@Serializable
data class GeocodeResult(
    val address_components: List<GeocodeAddressComponent>,
    val formatted_address: String,
    val geometry: GeocodeGeometry,
    val place_id: String,
    val plus_code: GeocodePlusCode,
    val types: List<String>
)

@Serializable
data class GeocodeAddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

@Serializable
data class GeocodeGeometry(
    val location: Location,
    val location_type: String,
    val viewport: GeocodeViewport
)

@Serializable
data class GeocodePlusCode(
    val compound_code: String,
    val global_code: String
)

@Serializable
data class GeocodeViewport(
    val northeast: Location,
    val southwest: Location
)