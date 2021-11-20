import {Card} from "@mui/material";
import {useEffect} from "react";
import {getVehicles} from "./api";
import CarOverlay from "./CarOverlay";

const Map = () => {
    // setup Google Maps
    useEffect(() => {
        let map = new google.maps.Map(document.getElementById('map')!, {
            center: {lat: 48.155004, lng: 11.4717963}, // munich
            zoom: 11
        });

        getVehicles().then(vehicles => {
            console.log(vehicles)
            for (let v of vehicles) {
                let overlay = new CarOverlay(new google.maps.LatLng(v.lat, v.lng));
                overlay.setMap(map)
            }
        });
    })

    return (
        <Card id="map" style={{height: "100%"}}>
        </Card>
    );
};

export default Map