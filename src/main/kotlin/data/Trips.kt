package data

object Trips {
    private val trips = mutableListOf<Trip>()

    fun getTrips() : List<Trip> {
        return trips
    }

    fun addTrip(trip: Trip) {
        trips.add(trip)
    }

    fun removeTrips(trips: List<Trip>) {
        this.trips.removeAll(trips)
    }
}