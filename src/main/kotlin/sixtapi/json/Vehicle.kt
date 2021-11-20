package sixtapi.json

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val charge: Int,
    val lat: Double,
    val lng: Double,
    val status: String,
    val vehicleID: String
)