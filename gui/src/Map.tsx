import {Card} from "@mui/material";
import {useEffect} from "react";
import {getBookings, getDirections, getVehicles} from "./api";

const Map = () => {
    // setup Google Maps
    useEffect(() => {
        let map = new google.maps.Map(document.getElementById('map')!, {
            center: {lat: 48.155004, lng: 11.4717963}, // munich
            zoom: 11
        });

        // display vehicles
        getVehicles().then(vehicles => {
            for (let v of vehicles) {
                new google.maps.Marker({
                    position: new google.maps.LatLng(v.lat, v.lng),
                    icon: "./taxi.png",
                    map: map
                })
            }
        });

        getBookings().then(bookings => {
            bookings.forEach(b =>
                getDirections({
                    startLat: b.pickupLat,
                    startLng: b.pickupLng,
                    dstLat: b.destinationLat,
                    dstLng: b.destinationLng
                }).then(result => {
                    console.log(result)

                    new google.maps.Polyline({
                        // this exists and we know it
                        // @ts-ignore
                        path: google.maps.geometry.encoding.decodePath(result.overview_polyline["points"]),
                        map: map
                    })

                })
            )
        })
    })

    return (
        <Card id="map" style={{height: "100%"}}>
        </Card>
    );
};

export default Map