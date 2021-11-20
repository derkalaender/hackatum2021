package data

import java.time.LocalDateTime
import kotlin.time.Duration

data class Trip(
    val startTime: LocalDateTime,
    val plannedEndTime: LocalDateTime,
    val assignedCar: Car?,
    val routes: MutableList<Route>,
    val timeBuffer: Duration
)