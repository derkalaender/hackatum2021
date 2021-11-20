//https://github.com/googlemaps/js-samples/blob/main/samples/react-map/src/index.tsx

import {EffectCallback, useEffect, useRef} from "react";
import {createCustomEqual} from "fast-equals";

const deepCompareEqualsForMaps = createCustomEqual(
    (deepEqual) => (a: any, b: any) => {
        if (
            a instanceof google.maps.LatLng ||
            b instanceof google.maps.LatLng
        ) {
            return new google.maps.LatLng(a).equals(new google.maps.LatLng(b));
        }

        // TODO extend to other types

        // use fast-equals for other objects
        return deepEqual(a, b);
    }
);

function useDeepCompareMemoize(value: any) {
    const ref = useRef();

    if (!deepCompareEqualsForMaps(value, ref.current)) {
        ref.current = value;
    }

    return ref.current;
}

export function useDeepCompareEffectForMaps(
    callback: EffectCallback,
    dependencies: any[]
) {
    useEffect(callback, dependencies.map(useDeepCompareMemoize));
}