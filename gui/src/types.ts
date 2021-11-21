export interface Vehicle {
    charge: number,
    lat: number,
    lng: number,
    status: VehicleStatus,
    vehicleID: string
}

export type VehicleStatus = "FREE" | "BOOKED"

export interface Booking {
    destinationLat: number,
    destinationLng: number,
    pickupLat: number,
    pickupLng: number,
    bookingID: string,
    vehicleID: string | null,
    status: BookingStatus
}

export type BookingStatus = "CREATED" | "STARTED"| "COMPLETED" | "VEHICLE_ASSIGNED"

export interface Location {
    lat: number
    lng: number
}

export interface Query {
    start: Location
    destination: Location
}

export interface RouteGeometry {
    path: Location[]
    polyline: string
}

export interface RouteMeta {
    CO2: number
    time: number
    distance: number
}

export interface RouteResult {
    id: string
    mergeID: string
    standardGeometry: RouteGeometry
    standardMeta: RouteMeta
    mergedGeometry: RouteGeometry
    mergedMeta: RouteMeta
}