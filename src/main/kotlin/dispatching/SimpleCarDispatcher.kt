package dispatching

import data.Car
import data.Route

object SimpleCarDispatcher : CarDispatcher {
    override fun getCar(route: Route): Car {
        return Car() // TODO
    }
}