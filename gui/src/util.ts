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