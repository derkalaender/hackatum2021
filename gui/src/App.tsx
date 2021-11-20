import {Box, Grid} from "@mui/material";
import Controls from "./Controls";
import React, {Suspense, useEffect, useState} from "react";
import {Status, Wrapper} from "@googlemaps/react-wrapper";
import {Vehicle} from "./types";
import {getBookings, getDirections, getVehicles} from "./api";
import Polyline from "./map/Polyline";
import Marker from "./map/Marker";

const Map = React.lazy(() => import("./map/Map"))

const render = (status: Status) => {
    switch (status) {
        case Status.LOADING:
            return <Box>Loading</Box>
        case Status.FAILURE:
            return <Box>Error</Box>
        case Status.SUCCESS:
            return <Map/>
    }
}

const App = () => {
    const [vehicles, setVehicles] = useState<Vehicle[]>([])
    const [bookedRoutes, setBookedRoutes] = useState<google.maps.LatLng[][]>([])

    // get vehicles
    useEffect(() => {
        getVehicles().then(vehicles => {
            setVehicles(vehicles)
        });
    }, [])

    // get booked routes
    useEffect(() => {
        getBookings().then(bookings => {
            Promise.all(bookings.map(b =>
                getDirections({
                    startLat: b.pickupLat,
                    startLng: b.pickupLng,
                    dstLat: b.destinationLat,
                    dstLng: b.destinationLng
                })
            )).then(routes => {
                // @ts-ignore
                setBookedRoutes(routes.map(r => google.maps.geometry.encoding.decodePath(r.overview_polyline["points"])))
            })
        })
    }, [])


    return (
        <Suspense fallback={<div>Loading...</div>}>
            <Grid container direction="column" style={{padding: "40px"}}>
                <Grid item>
                    <h1>Robotaxi</h1>
                </Grid>
                <Grid container spacing={5} alignItems="center" flexGrow={1}>
                    <Grid item xs={8} style={{height: "100%"}}>
                        <Wrapper apiKey="AIzaSyBXqG0fwm1zQ_XlPujzFV3OXbGkggCUBnA" render={render}
                                 libraries={["geometry"]}>
                            <Map center={{lat: 48.155004, lng: 11.4717963}} zoom={11}>
                                {vehicles.map(v => <Marker key={v.vehicleID}
                                                           position={new google.maps.LatLng(v.lat, v.lng)}
                                                           icon="taxi.png"/>)}
                                {bookedRoutes.map(v => <Polyline path={v}/>)}
                            </Map>
                        </Wrapper>
                    </Grid>
                    <Grid item xs={4}>
                        <Controls/>
                    </Grid>
                </Grid>
            </Grid>
        </Suspense>
    );
}

export default App;
