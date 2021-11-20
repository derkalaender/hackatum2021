import {Box, Grid} from "@mui/material";
import Map from "./Map";
import Controls from "./Controls";

const App = () => {
    return (
        <Box style={{height: "100%"}}>
            <Grid container spacing={5} alignItems="center" style={{height: "100%"}} sx={{p: 5}}>
                <Grid item xs={8} style={{height: "100%"}}>
                    <Map/>
                </Grid>
                <Grid item xs={4}>
                    <Controls/>
                </Grid>
            </Grid>
        </Box>
    );
}

export default App;
