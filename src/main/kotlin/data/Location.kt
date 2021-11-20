package data

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val lat: Double = 0.0,
    val lng: Double = 0.0
) {
    fun getUUID() : Double {
        return 0.5*(lat+lng)*(lat+lng+1)+lng
    }
}