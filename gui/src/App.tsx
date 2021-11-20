import {Box, Grid} from "@mui/material";
import Map from "./Map";
import Controls from "./Controls";

const App = () => {
    return (
        <Grid container direction="column" style={{padding:"40px"}}>
            <Grid item>
                <h1>Robotaxi</h1>
            </Grid>
            <Grid container spacing={5} alignItems="center" flexGrow={1}>
                <Grid item xs={8} style={{height:"100%"}}>
                    <Map/>
                </Grid>
                <Grid item xs={4}>
                    <Controls/>
                </Grid>
            </Grid>
        </Grid>
    );
}

export default App;
