import {Button, Card} from "@mui/material";
import {Vehicle} from "./types";

class USGSOverlay extends google.maps.OverlayView {
    private bounds: google.maps.LatLngBounds;
    private image: string;
    private div?: HTMLElement;

    constructor(bounds: google.maps.LatLngBounds, image: string) {
        super();

        this.bounds = bounds;
        this.image = image;
    }
}

const Map = () => {
    const onButton = async () => {
        let vehicles = await fetch("http://localhost:8080/vehicles")
            .then(res => res.json())
            .then(data => data as Vehicle[])
            .catch(err => console.log("error fetching"))

        console.log(vehicles)
    }

    return (
        <Card>
            <Button onClick={onButton}>Click me</Button>
        </Card>
    );
};

export default Map