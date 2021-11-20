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