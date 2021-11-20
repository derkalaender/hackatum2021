package sixtapi.json

import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val destinationLat: Double = 0.0,
    val destinationLng: Double = 0.0,
    val pickupLat: Double = 0.0,
    val pickupLng: Double = 0.0,
    val bookingID: String = "",
    val vehicleID: String? = null,
    val status: BookingStatus = BookingStatus.COMPLETED
)

@Serializable
enum class BookingStatus {
    CREATED, STARTED, COMPLETED, VEHICLE_ASSIGNED
}