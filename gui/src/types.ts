import LatLng = google.maps.LatLng;

export interface Vehicle {
    charge: number,
    lat: number,
    lng: number,
    status: string,
    vehicleID: string
}

export interface Booking {
    destinationLat: number,
    destinationLng: number,
    pickupLat: number,
    pickupLng: number,
    bookingID: string,
    vehicleID: string | null,
    status: BookingStatus
}

export enum BookingStatus {
    CREATED, STARTED, COMPLETED, VEHICLE_ASSIGNED
}

export interface Query {
    start: LatLng
    destination: LatLng
}

export interface RouteGeometry {
    path: LatLng[]
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