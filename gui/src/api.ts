import {Vehicle} from "./types"

export const getVehicles = async (): Promise<Vehicle[]> => {
    return await fetch("http://localhost:8080/vehicles")
        .then(res => res.json())
        .then(data => {
            return data as Vehicle[]
        })
        .catch(err => {
            console.log("error fetching")
            return []
        })
}