import {Button, Card, Grid, TextField} from "@mui/material";
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
}

export enum TextFieldId {
    START, DESTINATION
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
                                               onDestinationValueChange
                                           }) => {
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
                    <Button onClick={onSearch} disabled={searchEnabled === false}> Search</Button>
                </Grid>
                <Grid item>
                    <Button onClick={onSubmit} disabled={submitEnabled === false}>Submit</Button>
                </Grid>
            </Grid>
        </Card>
    )
}

export default Controls