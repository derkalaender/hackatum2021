package data

data class Bounds(
    val ne: Location,
    val sw: Location
)

data class Step(
    /**In meter*/
    val distance: Int,
    /**In seconds*/
    val duration: Int,
    val startLocation: Location,
    val endLocation: Location
)

data class Route(
    val bounds: Bounds,
    val steps: List<Step>,
    /**In meter*/
    val totalDistance: Int,
    /**In seconds*/
    val totalDuration: Int,
    val startLocation: Location,
    val endLocation: Location
)