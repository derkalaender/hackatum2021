import {Booking, Vehicle} from "./types"

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
        .then(data => {
            return data as Booking[]
        })
        .catch(() => {
            console.log("error fetching bookings")
            return []
        })
}

export const getDirections = async (directionsOptions: {
    startLat: number,
    startLng: number,
    dstLat: number,
    dstLng: number
}): Promise<google.maps.DirectionsRoute> => {
    return await fetch(baseUrl + "/directions", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(directionsOptions)
    })
        .then(res => res.json())
        .then(data => {
            return data as google.maps.DirectionsRoute
        })
        // .then(route => {
        //     // hack tochange the bounds type to work for javascript
        //     let currentBounds = route.bounds as unknown as {
        //         northeast: google.maps.LatLng,
        //         southwest: google.maps.LatLng
        //     }
        //     route.bounds = new google.maps.LatLngBounds(currentBounds.southwest, currentBounds.northeast)
        //     return route
        // })
        .catch(() => {
            console.log("error fetching bookings")
            return Promise.reject()
        })
}