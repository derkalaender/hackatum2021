import {Card} from "@mui/material";
import {useEffect} from "react";
import {Loader} from "@googlemaps/js-api-loader";
import styled from "@emotion/styled";

class CarOverlay extends google.maps.OverlayView {
    private lng: number;
    private lat: number;
    private div?: HTMLElement;

    constructor(lng: number, lat: number) {
        super();
        this.lng = lng;
        this.lat = lat;
    }

    onAdd() {
        this.div = document.createElement("div");
        this.div.style.borderStyle = "none";
        this.div.style.borderWidth = "0px";
        this.div.style.position = "absolute";
        this.div.style.backgroundColor = "red";
        this.div.style.width = "50px";
        this.div.style.height = "50px";

        // // Create the img element and attach it to the div.
        // const img = document.createElement("img");
        //
        // img.src = this.image;
        // img.style.width = "100%";
        // img.style.height = "100%";
        // img.style.position = "absolute";
        // this.div.appendChild(img);

        // Add the element to the "overlayLayer" pane.
        const panes = this.getPanes()!;

        panes.overlayLayer.appendChild(this.div);
    }
}

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