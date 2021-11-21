import {Location} from "./types";

export function createLatLng(string: string): google.maps.LatLng
export function createLatLng(lat: number, lng: number): google.maps.LatLng
export function createLatLng(latOrString: number | string, lng?: number): google.maps.LatLng {
    if (typeof latOrString == "string") {
        let split = latOrString.split(",")
        return new google.maps.LatLng(parseFloat(split[0].trim()), parseFloat(split[1].trim()))
    } else {
        return new google.maps.LatLng(latOrString, lng)
    }
}

export function createLocation(string: string): Location
export function createLocation(lat: number, lng: number): Location
export function createLocation(latOrString: number | string, lng?: number): Location {
    if (typeof latOrString == "string") {
        let split = latOrString.split(",")
        return {lat: parseFloat(split[0].trim()), lng: parseFloat(split[1].trim())}
    } else {
        return {lat: latOrString, lng: lng!}
    }
}

// random number between min and max (inclusive)
export const randomIntFromInterval = (min: number, max: number): number => {
    return Math.floor(Math.random() * (max - min + 1) + min)
}