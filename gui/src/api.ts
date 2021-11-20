import {Booking, Query, RouteGeometry, RouteResult, Vehicle} from "./types"

const baseUrl = "http://localhost:8080"

export const getVehicles = async (): Promise<Vehicle[]> => {
    return await fetch(baseUrl + "/vehicles")
        .then(res => res.json())
        .then(data => {
            return data as Vehicle[]
        })
        .catch(() => {
            console.log("error fetching vehicles")
            return []
        })
}

export const getBookings = async (): Promise<Booking[]> => {
    return await fetch(baseUrl + "/bookings")
        .then(res => res.json())
        .then(data => data as Booking[])
        .catch(() => {
            console.error("error fetching bookings")
            return []
        })
}

export const getDirections = async (query: Query): Promise<RouteGeometry> => {
    return await fetch(baseUrl + "/directions", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(query)
    })
        .then(res => res.json())
        .then(data => data as RouteGeometry)
        .catch(() => {
            console.error("error fetching bookings")
            return Promise.reject()
        })
}

export const searchRoutes = async (query: Query): Promise<RouteResult> => {
    return await fetch(baseUrl + "/route", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(query)
    })
        .then(res =>res.json())
        .then(data => data as RouteResult)
        .catch(() => {
            console.error("error searching route")
            return Promise.reject()
        })
}