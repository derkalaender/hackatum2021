package dispatching

import data.Car
import data.Route

interface CarDispatcher {
    fun getCar(route: Route) : Car
}