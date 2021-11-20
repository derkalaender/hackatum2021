package data

class Node(
    val location: Location,
    var car: Car?,
    var person: Person?,
) {
    fun getUUID() : Double {
        return location.getUUID()
    }
}

class Graph {
    val nodes = mutableMapOf<Double, Node>()
    val connections = mutableMapOf<Double, MutableList<Node>>()

    fun addTrip(trip: Trip) {

        //Create new nodes
        trip.routes
            .flatMap { it.steps }
            .map { listOf(it.startLocation, it.endLocation) }
            .flatten()
            .filter { it.getUUID() !in nodes.keys }
            .associate { Pair(it.getUUID(), Node(it, null, null)) }
            .let { nodes.putAll(it) }

        //Insert connections
        trip.routes
            .flatMap { it.steps }
            .forEach { step ->
                if (connections[step.startLocation.getUUID()] == null) connections[step.startLocation.getUUID()] = mutableListOf()
                val list = connections[step.startLocation.getUUID()]!!
                if (!list.any { step.endLocation.getUUID() == it.getUUID() }) list.add(nodes[step.endLocation.getUUID()]!!)
            }

        //Insert person position
        nodes[trip.startLocation.getUUID()]?.person = trip.person
    }

    /**Location must be known in graph*/
    fun updateCarLocation(car: Car, location: Location) {
        //find old location
        nodes.values.find { node -> node.car?.let { it == car } ?: false }?.car = null

        //update location
        nodes[location.getUUID()]?.car = car
    }

    fun insertRoute(route: Route) {
        route.steps
            .map { listOf(it.startLocation, it.endLocation) }
            .flatten()
            .filter { it.getUUID() !in nodes.keys }
            .associate { Pair }
    }
}