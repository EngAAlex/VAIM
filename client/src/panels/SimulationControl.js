/* 
 *  Copyright © 2020-2021 Alessio Arleo 
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

import React, { Component } from 'react';

import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { withStyles } from "@material-ui/core/styles";
//import PlayArrowIcon from '@material-ui/icons/PlayArrow';
import SkipNextIcon from '@material-ui/icons/SkipNext';
import SkipPreviousIcon from '@material-ui/icons/SkipPrevious';
import Slider from '@material-ui/core/Slider';
import Paper from '@material-ui/core/Paper';

//import ClearIcon from '@material-ui/icons/Clear';
//import BugReportIcon from '@material-ui/icons/BugReport';

//import VisibilityIcon from '@material-ui/icons/Visibility';
//import VisibilityOffIcon from '@material-ui/icons/VisibilityOff';

const SIMULATION_CONTROL_STYLE = {
    root: {
        display: "inline-block",
        width: "100%",
        textAlign: "center",
        padding: "5px"
    },
    buttons: {
        textAlign: "center",
        width: "100%"
    },
    frameText: {
        textAlign: "center"
    },
    simTable: {
        width: "100%"
    },
    simTableContainer: {
        display: "inline-block",
        border: "solid 1px black",
        borderRadius: "4px",
        marginLeft: "2px"
    },
    simTableHead: {
        borderBottom: "solid 1px black",
        backgroundColor: "lightgray"
    },
    simTableCell: {
        border: "solid 1px lightgrey",
        borderRadius: "4px"
    },
    sliderContainer: {
        width: "98%",
        marginLeft: "1%",
        border: "solid 0px lightgray",
        borderRadius: "19px",
        background: "rgba(0,0,0,0.1)"
    },
    buttonArea: {
        //textAlign: "center",
        width: "100%",
        display: "inline-block",
        padding: "3px",
        //marginTop: "5px",
        //marginBottom: "5px"
    },
    tablesContainer: {
        width: "100%",
        //display: "inline-block",
        //height: "auto",
        //position: "relative",
        //float: "right"
    },
    siminfoTableContainer: {
        width: "49%",
        display: "inline-block",
        border: "solid 1px black",
        borderRadius: "4px",
        cursor: "pointer"
    },
    commandsTableContainer: {
        width: "100%",
        border: "solid 1px black",
        borderRadius: "4px",
    },
    selectedSim: {
        borderBottom: "solid 1px black",
        backgroundColor: "yellow",
        fontWeight: "bold"
    },
    commandsTableHead: {
        borderBottom: "solid 1px black",
        backgroundColor: "lightgray",
        fontWeight: "bold"
    },
    commandsTableCell: {
        // border: "solid 1px lightgrey",
        // borderRadius: "4px",
        textAligh: "right",
        width: "100%"
    },
    simInfoHeader: {
        textAlign: "left",
        backgroundColor: "lightgrey",
        fontSize: "small"
    },
    leftButton: {
        width: "48%",
        marginRight: "2px"
    },
    paperLabel: {
        width: "100%",
        position: "relative",
        //float: "right",
        marginBottom: "5px"
    },
    sectionTitle:{
        textAlign: "center",
        fontSize: "medium",
        fontWeight: "bold",
        //fontStyle: "italic",
        width: "100%",
        background: "linear-gradient(180deg, #3f50b557, transparent)"
    },
    rightButton: {
        width: "48%",
        position: "relative",
        //float: "right"
    },
    ternaryButton: {
        width: "32%",
        display: "inline-block"
    }
}

class SimulationControl extends Component {

    constructor(props) {
        super(props);
        this.state = { selectedSim: -1 };
    }

    componentDidUpdate() {
        const { simulationsData, onChangedSelectedSimulation } = this.props;
        const { selectedSim } = this.state;

        if (selectedSim < 0 && simulationsData !== undefined && simulationsData.length > 0)
            this.setState({ ...this.state, selectedSim: simulationsData[0].id, simSeeds:  simulationsData[0].number_of_seeds} , 
                        () => onChangedSelectedSimulation(this.state.selectedSim, this.state.simSeeds));
    }

    render() {
        const { classes, simulationsData, simulationsContainer, updateViewOption,
            ff, rw, setFrame, firstFrame, lastFrame, aggregation, //play
            currentFrame, onChangedSelectedSimulation } = this.props;
        const { selectedSim } = this.state;

        if (simulationsData === undefined || simulationsData.length === 0 || selectedSim < 0)
            return "";
        
        if(simulationsContainer.get(selectedSim) === undefined){
            let newSim = simulationsContainer.keys().next().value;
            let seedsno = - 1;
            for(let s in simulationsData)
                if(s.id === newSim)
                    seedsno = s.number_of_seeds;
            this.setState({ ...this.state, selectedSim: newSim, simSeeds: seedsno }, 
                        () => onChangedSelectedSimulation(this.state.selectedSim, this.state.simSeeds));
            return "";
        }

        const { graphChunk, viewOptions } = simulationsContainer.get(selectedSim);
        const { /*peekSeeds,*/ showActiveEdges, showAllEdges, showLowInfection, showHighInfection } = viewOptions;
        let marks = [];
        //let simulationInfos = [];

        //simulationsData.forEach(simulationData =>
        //simulationInfos.push( 
        let simulationInfos = simulationsData.map((simulationData) =>
    (<div key={simulationData.id} simreference={simulationData.id} className={classes.siminfoTableContainer} seedsno={simulationData.number_of_seeds} onClick={(e) =>
            this.setState({ ...this.state, selectedSim: parseInt(e.currentTarget.getAttribute('simreference')), simSeeds: parseInt(e.currentTarget.getAttribute('seedsno')) }, 
                        () => onChangedSelectedSimulation(this.state.selectedSim, this.state.simSeeds))
        }>
            <table>
                <thead>
                    <tr>
                        <th className={classes.simInfoHeader}>
                            #
                                        </th>
                        <th className={simulationData.id === selectedSim ? classes.selectedSim : classes.commandsTableHead}>
                            {simulationData.id}
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td className={classes.simInfoHeader}>
                            Seeds
                                        </td>
                        <td className={classes.commandsTableCell}>
                            {simulationData.number_of_seeds}
                        </td>
                    </tr>
                    <tr>
                        <td className={classes.simInfoHeader}>
                            Active
                                        </td>
                        <td className={classes.commandsTableCell}>
                            (AVG) {(simulationData.avg_activated).toFixed(2)}
                        </td>
                    </tr>
                    <tr>
                        <td className={classes.simInfoHeader}>
                            STD.
                                        </td>
                        <td className={classes.commandsTableCell}>
                            {(simulationData.activated_stddev).toFixed(2)}
                        </td>
                    </tr>
                    <tr>
                        <td className={classes.simInfoHeader}>
                            Sel.
                                        </td>
                        <td className={classes.commandsTableCell}>
                            {simulationData.seed_selection}
                        </td>
                    </tr>
                    <tr>
                        <td className={classes.simInfoHeader}>
                            Model
                                        </td>
                        <td className={classes.commandsTableCell}>
                            {simulationData.diffusion_strategy}
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>)
        );
        for (let j = firstFrame; j <= lastFrame; j++)
            marks[j] = aggregation * j;
            /*  <td className={classes.simTableCell}><Button disabled onClick={() => play()}>
                    <PlayArrowIcon /></Button></td>*/
        return (
            <div>
                <Paper elevation={3} square className={classes.paperLabel}>
                    <Typography className={classes.sectionTitle}>Simulation Control</Typography>                            
                </Paper>
                <div className={classes.root}>
                    <table className={classes.buttons}>
                        <thead></thead>
                        <tbody>
                            <tr>
                                <td className={classes.simTableCell}><Button onClick={() => rw()} disabled={currentFrame === 0}>
                                    <SkipPreviousIcon /></Button></td>
                                <td className={classes.simTableCell}><Typography>Current Frame: {currentFrame}</Typography></td>
                                <td className={classes.simTableCell}><Button onClick={() => ff()} disabled={currentFrame === lastFrame}>
                                    <SkipNextIcon /></Button></td>
                            </tr>
                        </tbody>
                    </table>
                    <div className={classes.sliderContainer}>
                        <Slider style={{ width: "91%" }}
                            defaultValue={0}
                            aria-labelledby="discrete-slider"
                            valueLabelDisplay="auto"
                            step={1}
                            marks
                            min={firstFrame}
                            max={lastFrame}
                            onChangeCommitted={(d, value) => setFrame(value)}
                            value={currentFrame}
                        />
                    </div>
                </div>
                <div className={classes.root}>
                    <Paper elevation={3} square className={classes.paperLabel}>
                        <Typography className={classes.sectionTitle}>Loaded Simulations</Typography>
                    </Paper>
                    <div className={classes.tablesContainer}>
                        {simulationInfos}
                    </div>
                    <div className={classes.buttonArea}>

                        <Button
                            //style={{width: "49%", marginRight: "3px"}}
                            className={classes.leftButton}
                            disabled={graphChunk === undefined}
                            size="small"
                            variant="contained"
                            color={!showAllEdges ? "primary" : "secondary"}
                            //startIcon={!showAllEdges ? (<VisibilityIcon />) : (<VisibilityOffIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { showAllEdges: !showAllEdges })}
                        //onClick={(d) => this.setState({...this.state, showAllEdges: !showAllEdges})}
                        >{!showAllEdges ? "Show Edges" : "Hide Edges"}</Button>

                        <Button
                            //style={{width: "97%", float: "right"}}
                            className={classes.rightButton}
                            disabled={graphChunk === undefined}
                            size="small"
                            variant="contained"
                            color={!showActiveEdges ? "primary" : "secondary"}
                            //startIcon={!showActiveEdges ? (<BugReportIcon />) : (<ClearIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { showActiveEdges: !showActiveEdges })}
                        //onClick={(d) => this.setState({...this.state, showActiveEdges: !showActiveEdges})}
                        >{!showActiveEdges ? "Active edges" : "Hide Active"}</Button>

                        <Button
                            className={classes.leftButton}
                            size="small"
                            variant="contained"
                            color={!showLowInfection ? "primary" : "secondary"}
                            //startIcon={!showLowInfection ? (<VisibilityIcon />) : (<VisibilityOffIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { showLowInfection: !showLowInfection })}
                        >{!showLowInfection ? "Show Low" : "Hide Low"}</Button>

                        <Button
                            className={classes.rightButton}
                            size="small"
                            variant="contained"
                            color={!showHighInfection ? "primary" : "secondary"}
                            //startIcon={!showHighInfection ? (<VisibilityIcon />) : (<VisibilityOffIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { showHighInfection: !showHighInfection })}
                        >{!showHighInfection ? "Show  High" : "Hide  High"}</Button>

                    </div>
                </div>
            </div>
        );

    }

}

/*
                        <Button
                            //style={{width: "97%", marginRight: "3px"}}
                            className={classes.ternaryButton}
                            size="small"
                            variant="contained"
                            color={!peekSeeds ? "primary" : "secondary"}
                            //startIcon={!peekSeeds ? (<VisibilityIcon />) : (<VisibilityOffIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { peekSeeds: !peekSeeds })}
                        //onClick={(d) => this.setState({...this.state, peekSeeds: !peekSeeds})}
                        >{!peekSeeds ? "Peek Seeds" : "Stop Peek"}</Button>                        

<Button
                            //style={{width: "97%", marginRight: "3px"}}
                            className={classes.leftButton}
                            disabled={true}//{simulation.graphChunk === undefined}
                            size="small"
                            variant="contained"
                            color={localCoordinates ? "primary" : "secondary"}
                            startIcon={localCoordinates ? (<GpsFixedIcon />) : (<LocationSearchingIcon />)}
                            onClick={(d) => updateViewOption(selectedSim, { localCoordinates: !localCoordinates })}
                        //onClick={(d) => this.setState({...this.state, localCoordinates: !localCoordinates})}
                        >{localCoordinates ? "Local Coords." : "Global Coords."}</Button>
*/

const SimulationControlComponent = withStyles(SIMULATION_CONTROL_STYLE)(SimulationControl);

export default SimulationControlComponent;