package data

import java.time.LocalDateTime
import kotlin.time.Duration

data class PlannedTrip(
    val startTime: LocalDateTime,
    val plannedEndTime: LocalDateTime,
    val currentRoute: Route,
    val timeBuffer: Duration,
    val associatedTrips: List<Trip>,
    val isActive: Boolean,
    val assignedCar: Car?
)