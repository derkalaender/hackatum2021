import {Box, Grid} from "@mui/material";
import Controls, {DisplayValues, TextFieldId} from "./Controls";
import React, {ChangeEvent, useEffect, useState} from "react";
import {Status, Wrapper} from "@googlemaps/react-wrapper";
import {RouteResult, Vehicle} from "./types";
import {getBookings, getDirections, getVehicles, searchRoutes} from "./api";
import Polyline from "./map/Polyline";
import Marker from "./map/Marker";
import Map from "./map/Map";
import {createLatLng} from "./util";

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

const decodePolyline = (polyline: string): google.maps.LatLng[] => {
    return google.maps.geometry.encoding.decodePath(polyline)
}

const RoutResultRender = ({route, map}: { route: RouteResult, map?: google.maps.Map }) => {
    return (
        <>
            <Polyline path={decodePolyline(route.standardGeometry.polyline)} strokeColor={"red"} map={map}/>
            <Polyline path={decodePolyline(route.mergedGeometry.polyline)} strokeColor={"green"} map={map}/>
        </>
    )
}

const App = () => {
    const latlngRegex = "\\d+(\\.\\d+)?,\\w?.\\d+(\\.\\d+)?"

    const [vehicles, setVehicles] = useState<Vehicle[]>([])
    const [bookedRoutes, setBookedRoutes] = useState<google.maps.LatLng[][]>([])

    const [startLatLng, setStartLatLng] = useState<string>("")
    const [destinationLatLng, setDestinationLatLng] = useState<string>("")
    const [selectedTextField, setSelectedTextField] = useState<TextFieldId>(TextFieldId.START)

    const [routeResult, setRouteResult] = useState<RouteResult | null>(null)

    const correctLatLngFormat = (): boolean => {
        return [startLatLng, destinationLatLng].every(s => s.match(latlngRegex))
    }

    const latLngToMarker = (latlng: string, label: string) => {
        if (latlng.match(latlngRegex)) {
            let converted = createLatLng(latlng)
            return <Marker position={converted} label={label}/>
        }
    }

    const onStartValueChanged = (e: ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
        setStartLatLng(e.target.value)
    }

    const onDestinationValueChanged = (e: ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) => {
        setDestinationLatLng(e.target.value)
    }

    const onMapClick = (e: google.maps.MapMouseEvent) => {
        let latlng = e.latLng!!
        let coords = "" + latlng.lat() + ", " + latlng.lng()
        switch (selectedTextField) {
            case TextFieldId.START:
                setStartLatLng(coords)
                setSelectedTextField(TextFieldId.DESTINATION)
                break
            case TextFieldId.DESTINATION:
                setDestinationLatLng(coords)
                break
        }
    }

    const onSearch = () => {
        searchRoutes({
            start: createLatLng(startLatLng),
            destination: createLatLng(destinationLatLng)
        }).then(result => {
            console.log(result)
            setRouteResult(result)
        })
    }

    const onSubmit = () => {

    }

    const createDisplayValues = (): DisplayValues => {
        return {
            distanceStandard: routeResult?.standardMeta?.distance,
            timeStandard: routeResult?.standardMeta?.time,
            co2Standard: routeResult?.standardMeta?.CO2,
            distanceEco: routeResult?.mergedMeta?.distance,
            timeEco: routeResult?.mergedMeta?.time,
            co2Eco: routeResult?.mergedMeta?.CO2,
        }
    }

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
                    start: createLatLng(b.pickupLat, b.pickupLng),
                    destination: createLatLng(b.destinationLat, b.destinationLng),
                })
            )).then(routes => {
                // @ts-ignore
                setBookedRoutes(routes.map(r => decodePolyline(r.polyline)))
            })
        })
    }, [])


    return (
        <Grid container direction="column" style={{padding: "40px"}}>
            <Grid item>
                <h1>Robotaxi</h1>
            </Grid>
            <Grid container spacing={5} alignItems="center" flexGrow={1}>
                <Grid item xs={8} style={{height: "100%"}}>
                    <Wrapper apiKey="AIzaSyBXqG0fwm1zQ_XlPujzFV3OXbGkggCUBnA" render={render}
                             libraries={["geometry"]}>
                        <Map center={{lat: 48.155004, lng: 11.4717963}} zoom={11} onClick={onMapClick}>
                            {vehicles.map(v => <Marker key={v.vehicleID}
                                                       position={new google.maps.LatLng(v.lat, v.lng)}
                                                       icon="taxi.png"/>)}
                            {bookedRoutes.map(v => <Polyline path={v}/>)}

                            {latLngToMarker(startLatLng, "A")}
                            {latLngToMarker(destinationLatLng, "B")}
                            {routeResult && <RoutResultRender route={routeResult}/>}
                        </Map>
                    </Wrapper>
                </Grid>
                <Grid item xs={4}>
                    <Controls startValue={startLatLng}
                              destinationValue={destinationLatLng} onStartValueChange={onStartValueChanged}
                              onDestinationValueChange={onDestinationValueChanged} searchEnabled={correctLatLngFormat()}
                              submitEnabled={correctLatLngFormat()} onSelectTextField={setSelectedTextField}
                              selectedTextField={selectedTextField} onSearch={onSearch} onSubmit={onSubmit}
                              displayValues={createDisplayValues()}/>
                </Grid>
            </Grid>
        </Grid>
    );
}

export default App;
