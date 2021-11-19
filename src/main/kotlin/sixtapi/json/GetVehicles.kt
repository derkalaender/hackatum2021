package sixtapi.json

import kotlinx.serialization.Serializable

@Serializable
class GetVehicles : ArrayList<Vehicle>()

@Serializable
data class Vehicle(
    val charge: Int,
    val lat: Double,
    val lng: Double,
    val status: String,
    val vehicleID: String
)