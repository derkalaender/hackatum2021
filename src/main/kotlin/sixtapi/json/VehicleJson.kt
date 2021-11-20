package sixtapi.json

import kotlinx.serialization.Serializable

enum class VehicleStatus {
    SERVICE_BLOCK, FREE
}

@Serializable
data class Vehicle(
    val charge: Int,
    val lat: Double,
    val lng: Double,
    val status: VehicleStatus,
    val vehicleID: String
)