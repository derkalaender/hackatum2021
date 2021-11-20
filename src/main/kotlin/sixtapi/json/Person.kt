package sixtapi.json

import kotlinx.serialization.Serializable
import util.DurationSerializer
import kotlin.time.Duration

@Serializable
data class Person(
    @Serializable(DurationSerializer::class)
    val timeBuffer: Duration,
    val maxPeople: Int,
    val bookingID: String?
)