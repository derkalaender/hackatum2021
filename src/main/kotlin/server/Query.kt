package server

import data.Location
import kotlinx.serialization.Serializable

@Serializable
data class Query(
    val start: Location,
    val destination: Location
)