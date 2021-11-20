import {Button, Card} from "@mui/material";

const Map = () => {
    const onButton = () => {
        fetch("http://localhost:8080/vehicles")
            .then(res => res.json())
            .then(res => console.log(res))
            .catch(err => console.log("error fetching"));
    }

    return (
        <Card>
            <Button onClick={onButton}>Click me</Button>
        </Card>
    );
};

export default Map