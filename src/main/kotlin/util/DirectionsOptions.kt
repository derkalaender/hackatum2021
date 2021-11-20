package util

import kotlinx.serialization.Serializable

@Serializable
data class DirectionsOptions(
    val startLat: Double,
    val startLng: Double,
    val dstLat: Double,
    val dstLng: Double
)