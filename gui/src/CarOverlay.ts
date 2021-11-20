export default class CarOverlay extends google.maps.OverlayView {
    private latLng: google.maps.LatLng
    private div?: HTMLElement;

    constructor(latlng: google.maps.LatLng) {
        super();
        this.latLng = latlng;
    }

    onAdd() {
        this.div = document.createElement("div");
        this.div.style.borderStyle = "none";
        this.div.style.borderWidth = "0px";
        this.div.style.position = "absolute";
        this.div.style.width = "50px";
        this.div.style.height = "50px";

        // Create the img element and attach it to the div.
        const img = document.createElement("img");

        img.src = "./taxi.png";
        img.style.width = "100%";
        img.style.height = "100%";
        img.style.position = "absolute";
        this.div.appendChild(img);

        // Add the element to the "overlayLayer" pane.
        const panes = this.getPanes()!;

        panes.overlayLayer.appendChild(this.div);

    }

    draw() {
        // We use the south-west and north-east
        // coordinates of the overlay to peg it to the correct position and size.
        // To do this, we need to retrieve the projection from the overlay.
        const overlayProjection = this.getProjection();

        // retrieve pixel coords
        const coords = overlayProjection.fromLatLngToDivPixel(this.latLng)!;

        // Resize the image's div to fit the indicated dimensions.
        if (this.div) {
            this.div.style.left = coords.x - 50 / 2 + "px";
            this.div.style.top = coords.y - 50 / 2 + "px";
        }
    }

    /**
     * The onRemove() method will be called automatically from the API if
     * we ever set the overlay's map property to 'null'.
     */
    onRemove() {
        if (this.div) {
            (this.div.parentNode as HTMLElement).removeChild(this.div);
            delete this.div;
        }
    }

    /**
     *  Set the visibility to 'hidden' or 'visible'.
     */
    hide() {
        if (this.div) {
            this.div.style.visibility = "hidden";
        }
    }

    show() {
        if (this.div) {
            this.div.style.visibility = "visible";
        }
    }

    toggle() {
        if (this.div) {
            if (this.div.style.visibility === "hidden") {
                this.show();
            } else {
                this.hide();
            }
        }
    }

    toggleDOM(map: google.maps.Map) {
        if (this.getMap()) {
            this.setMap(null);
        } else {
            this.setMap(map);
        }
    }
}