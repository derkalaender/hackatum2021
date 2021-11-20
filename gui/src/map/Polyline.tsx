import {useEffect, useState} from "react";

const Polyline = (options: google.maps.PolylineOptions) => {
    const [polyline, setPolyline] = useState<google.maps.Polyline>();

    useEffect(() => {
        if (!polyline) {
            setPolyline(new google.maps.Polyline());
        }

        // remove polyline from map on unmount
        return () => {
            if (polyline) {
                polyline.setMap(null);
            }
        };
    }, [polyline]);

    useEffect(() => {
        if (polyline) {
            polyline.setOptions(options);
        }
    }, [polyline, options]);

    return null;
}

export default Polyline