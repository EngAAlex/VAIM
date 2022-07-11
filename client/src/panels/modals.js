/* 
 *  Copyright © 2021-2021 Alessio Arleo 
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

import React, {Component} from 'react';
import { withStyles } from '@material-ui/core/styles';
import {Paper, Typography, Modal, Button} from "@material-ui/core";
//import {CopyToClipboard} from 'react-copy-to-clipboard';
import {TextField, FormControlLabel, MenuItem, FormHelperText, Switch, FormControl, Select} from '@material-ui/core';
import RotateLeftIcon from '@material-ui/icons/RotateLeft';


/*function rand() {
  return Math.round(Math.random() * 20) - 10;
}*/

function getModalStyle() {
    const top = 50;// + rand();
    const left = 50;// + rand();
  
    return {
      top: `${top}%`,
      left: `${left}%`,
      transform: `translate(-${top}%, -${left}%)`,
    };
  }

const MODAL_STYLES =  {
    paper: {
        position: 'absolute',
        width: 700,
        backgroundColor: 'lightgrey',
        border: '2px solid #000',
        boxShadow: '3px',
        padding: '5px',
    },
    sectorTitle: {
        textAlign: "center"

    },
    modalHeader: {
        backgroundColor: "#3f51b5",
        color: "white",
        padding: "15px",
        marginBottom: "8px",
        fontWeight: "bold"
    },
    sectorHeading: {

    },
    individualSeed: {
        width: "10%",
        display: "inline-block",
        padding: "18px",
        borderTop: "solid 1px black",
        marginRight: "5px",
    },
    buttonBar: {
        width: '100%', 
        textAlign: 'center', 
        marginTop: '15px',        
    },    
    seedControl: {
        padding: "3px 15px 3px 15px",
        //marginLeft: "25%",
        borderTop: "solid 1px grey",
        borderRadius: "10px",
        textAlign: "center"
    }
};

class MessageModal extends Component{

    constructor(props){
        super(props);
        this.state = {show: false};
    }

    componentDidUpdate(prevProps){
        const {active} = this.props;
        if(prevProps.active !== active)
            this.setState({show: active});
    }

    render(){
        const {classes, message, title, onClose} = this.props;
        const {show} = this.state;

        return(
            <Modal 
            open={show}
            aria-labelledby="welcome-title"
            aria-describedby="welcome-description"
            onClose={() => onClose()}
            >
            <Paper style={getModalStyle()} className={classes.paper}>
                <Paper className={classes.modalHeader}>
                        <Typography variant="h6" className={classes.sectorTitle} id="welcome-title">{title}</Typography>
                </Paper>
                <Typography className={classes.sectorHeading} id="welcome-description">
                    {message}
                </Typography>  
                <div className={classes.buttonBar}>
                    <Button style={{width: "100%"}} onClick={() => this.setState({show: false}, () => onClose())} variant="contained">
                                Close 
                    </Button>
                </div>
            </Paper> 
        </Modal>   
        );
    }

}

class SeedsModal extends Component {

  constructor(props) {
      super(props);
      this.state = {acceptedSuggestions: 0};
  }

  componentDidUpdate(prevProps) {
    const {active, efficiency} = this.props;
    if(active !== prevProps.active)
        this.setState({...this.state, selectedAction: efficiency < 0.5 ? 1 : -1, acceptedSuggestions: 0});
  }

  render() {
    const {active, classes, structuralVertices, efficiency, nodesList, onClose} = this.props;
    const {acceptedSuggestions, selectedAction} = this.state;    

    let finalList = [];
    let preamble = "", parsedEfficiency = 0;
    if(nodesList !== undefined){
        /*finalList = nodesList.filter((d, ind) => {
            if(efficiency < 0.5)
                return d.frames === null || d.frames === undefined || d.frames[0] === undefined || d.frames[0] === null || d.frames[0] === 0;//!d.isSeed;
            else
                return d.frames !== undefined && d.frames !== null && d.frames[0] !== undefined && d.frames[0] !== null;
        })*/
        finalList = (selectedAction < 0 ? nodesList.seeds : nodesList.nonseeds).slice(0,20).map((d, ind) =>{
            return <Typography key={ind} className={classes.individualSeed}>Id: {d.id} Out-Deg: {structuralVertices.get(d.id).degree}</Typography>
        });        
        //preamble = efficiency < 0.5 ? "Add to " : "Remove From";
        preamble = selectedAction > 0 ? "Add to " : "Remove From";
        parsedEfficiency = parseInt(efficiency*100);
    }else
        finalList = (<p style={{textAlign:"center", fontStyle: "italic"}}>There are no suggestions available.</p>);

    return(
            <Modal 
                open={active}
                aria-labelledby="welcome-title"
                aria-describedby="welcome-description"
            >
                <Paper style={getModalStyle()} className={classes.paper}>
                <Paper className={classes.modalHeader}>
                    <Typography variant="h6" className={classes.sectorTitle} id="welcome-title">Seed Suggestion</Typography>
                </Paper>
                <Typography className={classes.sectorHeading} id="welcome-description">
                    The selected cell has an efficiency of {parsedEfficiency}%. We suggest a list of nodes (in order of relevance) 
                    that are candidate to be {parsedEfficiency > 50 ? "removed" : "considered"} as seeds in the next simulation. 
                </Typography>              
                <Typography className={classes.sectorSubtitle}>
                    The first 20 are shown here. To accept more (or less) of the suggestions, use the select box below.
                </Typography>
                    <div style={{textAlign: "center"}}>
                        {finalList}
                    </div>
                    <div className={classes.seedControl}>
                        <FormControl>
                            <FormControlLabel
                                            control={<TextField value={acceptedSuggestions} type='number' 
                                                onChange={(event) => this.setState({...this.state, acceptedSuggestions: Math.abs(event.target.value < 0 ? 0 : event.target.value%(finalList.length+1))})} name="Nodes" />}
                                            label={"Number of Seeds to"}
                                        />
                            </FormControl>
                        <Select
                            id="seed-strategy-select"
                            value={selectedAction}
                            onChange={(event) => this.setState({...this.state, acceptedSuggestions: Math.abs(acceptedSuggestions%(event.target.value > 0 ? nodesList.nonseeds.length + 1 : nodesList.seeds.length + 1)), selectedAction: event.target.value})}
                            className={classes.SelectAction}
                            inputProps={{ 'aria-label': 'Without label' }}
                        >
                            <MenuItem key={-1} value={-1}>Remove</MenuItem>
                            <MenuItem key={1} value={1}>Add</MenuItem>
                        </Select>
                        <br/>
                        <Typography variant="caption" style={{fontStyle: "italic"}}>
                            You can switch back to {selectedAction < 0 ? "add" : "remove"} seeds by using the select box here. <br/>
                            Nodes suggestions and ordering will change accordingly.
                        </Typography>
                    </div>
                    <div className={classes.buttonBar}>
                        <Button style={{width: '49%', display: 'inline-block'}} onClick={() => onClose()} variant="contained">
                            Close 
                        </Button>
                        <Button disabled={nodesList === undefined || nodesList.length === 0 || acceptedSuggestions === 0} style={{width: '49%', display: 'inline-block', position:"relative", float: "right"}} onClick={() => onClose({type: /*efficiency < 0.5 ? 1 : -1*/ selectedAction, value: acceptedSuggestions})} variant="contained" color="primary">{preamble} SeedSet</Button>
                    </div>
                </Paper>
            </Modal>
    );
  }


  _textFromArray = (array) => {
    let result = "";
    for(let s in array)
        if(s === "0")
            result += array[s].id;
        else
            result += ","+array[s].id;
    return result;
  }
}

class SimulationModal extends Component {

    constructor(props){
        super(props);
        this.state = {randomSeeds: (props.currentSimulation === undefined ? true : false), noOfRandomSeeds: 0, availableModels: props.availableModels, availableStrategies: props.availableStrategies, selectedModel: {key: "", readable: "", empty: true}, selectedStrategy: {key: "", readable: "", empty: true}};
    }

    componentDidUpdate(prevProps){;
        const {selectedModel, selectedStrategy} = this.state;
        const {availableModels, availableStrategies} = this.props;
        if(prevProps.availableModels !== availableModels ||
            prevProps.availableStrategies !== availableStrategies){
            let tempState = {...this.state,
                availableModels: availableModels, 
                availableStrategies: availableStrategies};
            if(selectedModel.empty)
                tempState = {...tempState, selectedModel: availableModels[0].key};
            if(selectedStrategy.empty)
                tempState = {...tempState, selectedStrategy: availableStrategies[0].key};            
            this.setState(tempState);
        }
    }

    render (){
        const {randomSeeds, noOfRandomSeeds,
               selectedModel, selectedStrategy, availableModels, availableStrategies} = this.state;
        const {classes, active, currentSimulation, currentSimulationNoOfSeeds,  
               onPanelClose, seedsMods, onSeedsModsReset, noOfVertices} = this.props;
        let arrModels = [];
        let arrStrategies = [];
        let secondPart = "";
        let seedsModsText = "";
        let currentSeedSize;
        if(currentSimulation !== undefined && currentSimulation !== null) {
            currentSeedSize = currentSimulationNoOfSeeds;
        }
        for(let s of availableModels)
            arrModels.push(
                <MenuItem key={s.key} value={s.key}>{s.readable}</MenuItem>
            );
        for(let s of availableStrategies)
            arrStrategies.push(
                <MenuItem key={s.key} value={s.key}>{s.readable}</MenuItem>
            ); 
        for(let s of seedsMods){
            currentSeedSize += s.value * s.type;
            seedsModsText += `${s.type < 0 ? "Removed" : "Added"} ${s.value} seeds from cell ${s.sectorId}\t`
        }
        if(randomSeeds || currentSimulation === undefined || currentSimulation === null)
                secondPart = (
                <div>
                    <FormControl style={{display: "inline-block", width: "50%"}}>
                        <TextField
                            id="seedNumber"
                            label="# of Seeds"
                            type="number"
                            value={noOfRandomSeeds}
                            InputLabelProps={{
                                shrink: true,
                            }}
                            onChange={(event) => this.setState({...this.state, noOfRandomSeeds: event.target.value < 0 ? 0 : (event.target.value > noOfVertices ? noOfVertices : event.target.value)})}
                        />
                        <FormHelperText id="seedNumber">Select the number of seeds to pick</FormHelperText>
                    </FormControl>
                    <FormControl style={{display: "inline-block", width: "50%"}}>
                        <Select
                            id="seed-strategy-select"
                            value={selectedStrategy}
                            onChange={(event) => this.setState({...this.state, selectedStrategy: event.target.value})}
                            defaultValue={selectedStrategy}
                            /*displayEmpty
                            className={classes.selectEmpty}*/
                            inputProps={{ 'aria-label': 'Without label' }}
                        >
                            {arrStrategies}
                        </Select>
                        <FormHelperText id="seed-strategy-select">Select the Seed Selection Strategy</FormHelperText>
                    </FormControl>
                </div>
                );
        else
            secondPart = (
                <div style={{marginTop: "4px"}}>
                    <div>
                        <Typography style={{display:"inline-block"}}>Initial Seed budget: {currentSimulationNoOfSeeds}</Typography>
                        <Typography style={{display:"inline-block", position: "relative", float: "right"}}>Current Estimated budget: {currentSeedSize}</Typography>
                    </div>
                    <div style={{textAlign: "right", marginTop: "5px"}}>
                        <TextField multiline style={{width: "100%"}} id="outlined-basic" label="SeedSet Modifications" variant="outlined" disabled value={seedsModsText}/>
                        <Button
                            variant="contained"
                            color="secondary"
                            className={classes.resetButton}
                            endIcon={<RotateLeftIcon />}
                            onClick={() => onSeedsModsReset()}    
                            size="small"                        
                        >
                            Reset
                        </Button>
                    </div>
                    <Typography style={{width: "100%", textAlign: "center"}}>These modifications will be applied to the selected simulation seed set when clicking Execute.</Typography>
                </div>
            );
        return (<Modal 
            open={active}
            aria-labelledby="welcome-title"
            aria-describedby="welcome-description"
        >
            <Paper style={getModalStyle()} className={classes.paper}>
                <Paper className={classes.modalHeader}>
                    <Typography variant="h6" className={classes.sectorTitle} id="welcome-title">Request New Simulation</Typography>
                </Paper>
                <Typography className={classes.sectorHeading} id="welcome-description">
                   Request new Simulation with the following Settings
                </Typography>
                <div style={{marginBottom: "10px"}}>
                    <FormControl style={{display: "inline-block", width: "50%"}}>
                        <Select
                        value={selectedModel}
                        defaultValue={selectedModel}
                        onChange={(event) => this.setState({...this.state, selectedModel: event.target.value})}
                        //displayEmpty
                        //className={classes.selectEmpty}
                        inputProps={{ 'aria-label': 'Without label' }}
                        >
                            {arrModels}
                        </Select>
                        <FormHelperText id="model-helper-text">Select the Diffusion Model</FormHelperText>
                    </FormControl>
                    <FormControl style={{display: "inline-block", width: "50%"}}>
                            <FormControlLabel
                                control={<Switch checked={randomSeeds || currentSimulation === undefined || currentSimulation === null} disabled={currentSimulation === undefined || currentSimulation === null} onChange={(event) => this.setState({...this.state, randomSeeds: event.target.checked})} name="gilad" />}
                                label="Auto Seed Selection"
                            />
                    </FormControl>
                </div>
                <div style={{marginBottom: "10px"}}>
                    {secondPart}                    
                </div>
                <div className={classes.buttonBar}>
                    <Button style={{width: '49%', display: 'inline-block'}} onClick={() => onPanelClose()} variant="contained">
                        Close 
                    </Button>
                    <Button style={{width: '49%', display: 'inline-block', position:"relative", float: "right"}} disabled={(!randomSeeds && currentSeedSize <= 0) || (randomSeeds && noOfRandomSeeds <= 0)}
                                onClick={() => onPanelClose({selectedModel: selectedModel, selectedStrategy: selectedStrategy,
                                randomSeeds: (randomSeeds || currentSimulation === undefined || currentSimulation === null) ? noOfRandomSeeds : undefined})} 
                                variant="contained" color="primary">
                        Execute 
                    </Button>                
                </div>
            </Paper>
        </Modal>);
    }

}

const MessageModalPanel = withStyles(MODAL_STYLES)(MessageModal);
const SeedsModalPanel = withStyles(MODAL_STYLES)(SeedsModal);
const SimulationModalPanel = withStyles(MODAL_STYLES)(SimulationModal);
export {SeedsModalPanel, SimulationModalPanel, MessageModalPanel};