package data

import routefinding.RouteSearchResult
import sixtapi.SixtAPI

object PendingRoutes {
    private val map = mutableMapOf<String, RouteSearchResult>()

    fun addRoute(id: String, route: RouteSearchResult) {
        map[id] = route
    }

    suspend fun registerRoute(id: String, selectMerge: Boolean) {
        val routes = map[id] ?: return

        if(selectMerge) {
            if(routes.booking != null) SixtAPI.deleteBooking(routes.booking.bookingID)
            if(routes.mergedRoute != null)
            kotlin.runCatching {
                val (startLat, startLng) = routes.mergedRoute.legs.first().start_location
                val (destLat, destLng) = routes.mergedRoute.legs.last().end_location
                SixtAPI.postBooking(startLat, startLng, destLat, destLng, id)
            }
        } else {
            kotlin.runCatching {
                val (startLat, startLng) = routes.standardRoute!!.legs.first().start_location
                val (destLat, destLng) = routes.standardRoute.legs.last().end_location
                SixtAPI.postBooking(startLat, startLng, destLat, destLng, id)
            }
        }
    }

    fun deleteRoute(id: String) {
        map.remove(id)
    }
}