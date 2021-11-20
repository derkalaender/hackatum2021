import {Button, Card} from "@mui/material";
import {getVehicles} from "./api";

const Map = () => {
    const onButton = async () => {
        let vehicles = await getVehicles();

        console.log(vehicles);
    }

    return (
        <Card>
            <Button onClick={onButton}>Click me</Button>
        </Card>
    );
};

export default Map