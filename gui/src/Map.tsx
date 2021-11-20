import {Card, Container} from "@mui/material";
import {useEffect} from "react";
import {Loader} from "@googlemaps/js-api-loader";
import styled from "@emotion/styled";

const MapContainer = styled.div`
    height: 100%;
`

const Map = () => {
    // setup Google Maps
    useEffect(() => {
        const loader = new Loader({
            apiKey: "AIzaSyBXqG0fwm1zQ_XlPujzFV3OXbGkggCUBnA",
            version: "weekly",
        });

        loader.load().then(() => {
            let map = new google.maps.Map(document.getElementById('map')!!, {
                center: {lat: 48.155004, lng: 11.4717963}, // munich
                zoom: 11
            });
        })
    })

    return (
        <Card id="map" style={{height: "100%"}}>
        </Card>
    );
};

export default Map