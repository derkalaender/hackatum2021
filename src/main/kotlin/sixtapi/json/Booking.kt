package sixtapi.json

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val destinationLat: Double,
    val destinationLng: Double,
    val pickupLat: Double,
    val pickupLng: Double,
    val bookingID: String,
    val vehicleID: String? = null,
    val status: BookingStatus
)

@Serializable
enum class BookingStatus {
    CREATED, STARTED, COMPLETED, VEHICLE_ASSIGNED
}