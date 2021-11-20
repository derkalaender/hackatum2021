package sixtapi.json

import kotlinx.serialization.Serializable

enum class VehicleStatus {
    SERVICE_BLOCK, FREE;

    companion object {
        fun stringToStatus(string: String) : VehicleStatus {
            return values().find { it.name == string } ?: SERVICE_BLOCK
        }
    }
}

data class Vehicle(
    val charge: Int,
    val lat: Double,
    val lng: Double,
    val status: VehicleStatus,
    val vehicleID: String
) {
    fun toJson() : VehicleJson {
        return VehicleJson(
            charge,
            lat,
            lng,
            status.name,
            vehicleID
        )
    }
}

@Serializable
data class VehicleJson(
    val charge: Int,
    val lat: Double,
    val lng: Double,
    val status: String,
    val vehicleID: String
)