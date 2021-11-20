package data

object PlannedTrips {
    private val trips = mutableListOf<PlannedTrip>()

    fun getTrips() : List<PlannedTrip> {
        return trips
    }

    fun addTrip(trip: PlannedTrip) {
        trips.add(trip)
    }

    fun removeTrips(trips: List<PlannedTrip>) {
        this.trips.removeAll(trips)
    }
}