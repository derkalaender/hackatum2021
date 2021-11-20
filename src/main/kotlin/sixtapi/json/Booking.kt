package sixtapi.json

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val destinationLat: Double,
    val destinationLng: Double,
    val pickupLat: Double,
    val pickupLng: Double
)