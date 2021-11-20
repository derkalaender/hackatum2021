//https://github.com/googlemaps/js-samples/blob/main/samples/react-map/src/index.tsx

import {Card} from "@mui/material";
import React, {useEffect, useRef, useState} from "react";
import {useDeepCompareEffectForMaps} from "./util";

interface MapProps extends google.maps.MapOptions {
    onClick?: (e: google.maps.MapMouseEvent) => void;
    onIdle?: (map: google.maps.Map) => void;
}

const Map: React.FC<MapProps> = ({onClick, onIdle, children, ...options}) => {
    const ref = useRef<HTMLDivElement>(null);
    const [map, setMap] = useState<google.maps.Map>();

    // setup Google Maps
    useEffect(() => {
        if (ref.current && !map) {
            setMap(new google.maps.Map(ref.current))
        }
    }, [ref, map]);

    useDeepCompareEffectForMaps(() => {
        if (map) {
            map.setOptions(options);
        }
    }, [map, options]);

    // listener
    useEffect(() => {
        if (!map) return

        ["click", "idle"].forEach((eventName) =>
            google.maps.event.clearListeners(map, eventName)
        );

        if (onClick) {
            map.addListener("click", onClick);
        }

        if (onIdle) {
            map.addListener("idle", () => onIdle(map));
        }
    })

    return (
        <Card ref={ref} style={{height: "100%"}}>
            {React.Children.map(children, child => {
                if (React.isValidElement(child)) {
                    return React.cloneElement(child, {map})
                }
            })}
        </Card>
    );
};

export default Map