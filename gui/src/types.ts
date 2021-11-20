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

enum BookingStatus {
    CREATED, STARTED, COMPLETED, VEHICLE_ASSIGNED
}