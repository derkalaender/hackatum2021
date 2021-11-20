import {Grid} from "@mui/material";
import Controls from "./Controls";
import React, {Suspense, useEffect, useState} from "react";
import {Loader} from "@googlemaps/js-api-loader";

const Map = React.lazy(() => import("./Map"))

const App = () => {
    const [mapLoaded, setMapLoaded] = useState(false);

    // wait for google maps to load and only then import the Map Component
    useEffect(() => {
        const loader = new Loader({
            apiKey: "AIzaSyBXqG0fwm1zQ_XlPujzFV3OXbGkggCUBnA",
            version: "weekly",
            libraries: ["geometry"]
        });
        loader.load().then(() => {
            setMapLoaded(true)
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
                        {
                            mapLoaded && <Map/>
                        }
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
