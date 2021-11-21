import {Button, Card, Chip, Grid, TextField, Tooltip} from "@mui/material";
import React, {ChangeEventHandler} from "react";

interface ControlsProps {
    onSearch?: () => void
    searchEnabled?: boolean
    onSubmit?: () => void
    submitEnabled?: boolean
    onSelectTextField?: (id: TextFieldId) => void
    selectedTextField?: TextFieldId
    startValue?: string
    onStartValueChange?: ChangeEventHandler<HTMLTextAreaElement | HTMLInputElement>
    destinationValue?: string
    onDestinationValueChange?: ChangeEventHandler<HTMLTextAreaElement | HTMLInputElement>
    displayValues?: DisplayValues
}

export enum TextFieldId {
    START, DESTINATION
}

export interface DisplayValues {
    distanceStandard?: number
    distanceEco?: number
    timeStandard?: number
    timeEco?: number
    co2Standard?: number
    co2Eco?: number
}

const Controls: React.FC<ControlsProps> = ({
                                               onSearch,
                                               searchEnabled,
                                               onSubmit,
                                               submitEnabled,
                                               onSelectTextField,
                                               selectedTextField,
                                               startValue,
                                               onStartValueChange,
                                               destinationValue,
                                               onDestinationValueChange,
                                               displayValues
                                           }) => {

    const displayDistance = (number: number | undefined): string => {
        if (number === undefined) return "N/A"

        return (number / 1000).toFixed(2) + "km"
    }

    const displayTime = (number: number | undefined): string => {
        if (number === undefined) return "N/A"

        return (number / 60 / 60).toFixed(2) + "h"
    }

    const calcCO2Saved = (): string => {
        if (displayValues?.co2Standard === undefined || displayValues?.co2Eco === undefined) return "N/A"

        return ((1 - displayValues.co2Eco / displayValues.co2Standard) * 100).toFixed(0) + "%"
    }

    const toolTipCO2 = () => {
        if (displayValues?.co2Standard === undefined || displayValues?.co2Eco === undefined) return "N/A"

        return <>
            <p>
                A normal trip would cost the environment a total of {displayValues.co2Standard.toFixed(2)} kg of CO2.
            </p>
            <p>
                But with carpooling it's only {displayValues.co2Eco.toFixed(2)} kg of CO2!
            </p>
        </>

    }

    return (
        <Card sx={{p: 5}}>
            <Grid container spacing={2} direction="column" alignItems="center" alignContent="center">
                <Grid item>
                    <TextField label="Start" onSelect={() => onSelectTextField?.(TextFieldId.START)} value={startValue}
                               onChange={onStartValueChange} focused={selectedTextField === TextFieldId.START}/>
                </Grid>
                <Grid item>
                    <TextField label="Destination" onSelect={() => onSelectTextField?.(TextFieldId.DESTINATION)}
                               value={destinationValue}
                               onChange={onDestinationValueChange}
                               focused={selectedTextField === TextFieldId.DESTINATION}/>
                </Grid>
                <Grid item>
                    <Button onClick={onSearch} disabled={searchEnabled === false} variant="contained"> Search</Button>
                </Grid>
                {/*<Grid item>*/}
                {/*    <Button onClick={onSubmit} disabled={submitEnabled === false}>Submit</Button>*/}
                {/*</Grid>*/}

                <Grid item>
                    <Chip label={"Distance normal: " + displayDistance(displayValues?.distanceStandard)}/>
                </Grid>
                <Grid item>
                    <Chip label={"Time normal: " + displayTime(displayValues?.timeStandard)}/>
                </Grid>
                <Grid item>
                    <Chip label={"Distance Carpooling: " + displayDistance(displayValues?.distanceEco)}/>
                </Grid>
                <Grid item>
                    <Chip label={"Time Carpooling: " + displayTime(displayValues?.timeEco)}/>
                </Grid>
                <Grid item>
                    <Tooltip title={toolTipCO2()}>
                        <Chip label={"CO2 saved: " + calcCO2Saved()} color="success"/>
                    </Tooltip>

                </Grid>

            </Grid>
        </Card>
    )
}

export default Controls