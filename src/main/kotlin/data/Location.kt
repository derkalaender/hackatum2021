package data

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lng: Double = 0.0,
    val lat: Double = 0.0
)