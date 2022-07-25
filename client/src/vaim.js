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

import React, {Component} from 'react';

import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import LeakAddIcon from '@material-ui/icons/LeakAdd';
import Badge from '@material-ui/core/Badge';
import AcUnitIcon from '@material-ui/icons/AcUnit';
import BugReportIcon from '@material-ui/icons/BugReport';
import CachedIcon from '@material-ui/icons/Cached';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import {withStyles} from "@material-ui/core/styles";
import Grid from '@material-ui/core/Grid';
import * as d3 from 'd3';

import MatrixComponent from './diagrams/matrix.js';
import {executeFetch, endpoints} from './utils/dataLoader.js';
import {GraphSelector, SimulationsSelector} from './panels/drawers.js';
import SimulationSmallMultiple from './panels/SimulationSmallMultiple.js';
import {SimulationModalPanel, MessageModalPanel} from './panels/modals.js'
import {LoadingAnimation} from './panels/loading_spinner.js'
import SimulationControlComponent from './panels/SimulationControl';
import ResolutionSliderComponent from './panels/resolutionSlider';
import {getSuffixString} from "./utils/utils";

const CURR_VERSION = "250722A";

const AVAILABLE_MODELS = [
    {key: "IndependentCascadeModel", readable: "Independent Cascade"},
    {key: "LinearThreshold", readable: "Linear Threshold"}
];

/*const AVAILABLE_STRATEGIES = [
    {key: "Random", readable: "Random Selection"}
];*/

/*const APPBAR_STYLES = {
    root: {
        minHeight: "40px"
    }
}*/

export const VAIN_STYLES = {
    gridRoot: {
        width: "100%",
        //height: `${window.innerHeight - 64}px`,
        marginTop: "5px"
    },
    gridFirstRow: {
        //height: "11%",
        //padding: "6px",
        //borderRight: "solid 1px black"
    },
    gridMatrixRow: {
        padding: "3px"
        //height: "61%",
        //padding: "6px",
        //borderRight: "solid 1px black"
    },
    gridSecondRow: {
        //height: "25%",
        //padding: "6px",
        //borderRight: "solid 1px black"
    },
    title:{
        fontWeight: "bold"
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
    centeredText:{
        marginTop: "50%",
        textAlign: "center"
    },
    adjMatrixContainer: {
        width: "100%",
        height: "100%"
    },
    singleChunk: {
        display: "inline-grid",
        width: "100%",
        border: "solid 1px black"
    },
    matrixResolution: {
        width: "100%",
        textAlign: "center",        
    },
    inline: {
        display: "inline-grid"
    },
    simTable: {
        width: "100%"
    },
    simTableContainer: {
        display: "inline-block",
        border: "solid 1px black",
        borderRadius: "4px",
        marginLeft: "2px",
        width: "100%"
    },
    simTableHead: {
        borderBottom: "solid 1px black",
        backgroundColor: "lightgray"
    },
    simTableCell: {
        border: "solid 1px lightgrey",
        borderRadius: "4px"
    },
    version: {
        fontSize: "small",
        fontStyle: "italic",
        position: "absolute",
        top: "30px",
        right: "10px"
    },
    showTopButton:{
        backgroundColor: "#3f51b5", zIndex: "9999",
        color: "white", position: "absolute", 
        top: "5px", right: "5px", 
        width: "50px", height: "50px", 
        borderRadius: "25px", fontSize: "large"
    }
};

const RESOLUTIONS = [8, 10, 12, 14];
const DEFAULT_MATRIXRES = 1;

class Vaim extends Component{

    constructor(props){
        super(props);
        this.state = {
            firstRun: true,
            availableGraphs: [],
            availableMaximizations: [{key: "", readable: ""}],
            currentGraphData: {},
            loadedSimulations: new Map(),
            maxInfectedInSims: 0,
            graphsSideMenu: false,
            simulationsSideMenu: false,
            simulationModal: false,
            loading: true,
            loadingText: "Loading...",
            messageModal: false,
            messageModalText: "",
            messageModalTitle: "",
            maxSimsFrames: Number.MIN_SAFE_INTEGER,
            maxActiveInSims: Number.MIN_SAFE_INTEGER,
            seedsMods: [],
            selectedSimulation: undefined,
            selectedSimulationNoOfSeeds: undefined,
            visCfg: {
                matrixResolution: RESOLUTIONS[DEFAULT_MATRIXRES],
                firstFrame: 0,
                currentFrame: 0,
                lastFrame: Number.MIN_SAFE_INTEGER,
                clickedSectors: null,
            },
            hideTopBar: false
        };
    }

    _resetGraphAndReload = (callback) => {
        this.setState({...this.state, 
            currentGraphData: {},
            loadedSimulations: new Map(),
            maxInfectedInSims: 0,
            graphsSideMenu: false,
            simulationsSideMenu: false,
            simulationModal: false,
            loading: true,
            loadingText: "Loading...",
            messageModal: false,
            messageModalText: "",
            messageModalTitle: "",
            maxSimsFrames: Number.MIN_SAFE_INTEGER,
            maxActiveInSims: Number.MIN_SAFE_INTEGER,
            seedsMods: [],
            selectedSimulation: undefined,
            selectedSimulationNoOfSeeds: undefined,
            visCfg: {...this.state.visCfg,
                firstFrame: 0,
                currentFrame: 0,
                lastFrame: Number.MIN_SAFE_INTEGER,
                clickedSectors: null,
            }
            }, callback);
    }

    componentDidMount = async () => {
        await executeFetch(endpoints.LOAD_AVAILABLE()).then((data) => {
            if(data.err === undefined)
                this.setState({...this.state, loading: false, availableGraphs: data.fetchedData.graphlist !== null ? data.fetchedData.graphlist : [], availableMaximizations: data.fetchedData.maximizations});
            else
                this._failCommunication();
        });
    }

    _loadGraph = async (data) => {
        if(data === null)
            this.setState({...this.state, graphsSideMenu: false});
        else {
            const {graph_id} = data;
            this._resetGraphAndReload( async () => {
                const {visCfg} = this.state;
                await executeFetch(endpoints.LOAD_GRAPH(graph_id, visCfg.matrixResolution)).then((data) => {
                    if(data.err === undefined){
                        let simulationsMap = new Map();
                        if(data.fetchedData.simulations !== undefined && data.fetchedData.simulations !== null)
                        data.fetchedData.simulations.forEach((d, ind) => {
                            simulationsMap.set(d.id, ind);
                        })
                        data.fetchedData.vertices = [];
                        this.setState({...this.state, loading: false, simulationsMap: simulationsMap, 
                           loadedSimulations: new Map(),
                           visCfg: {...visCfg, firstFrame: 0,
                            currentFrame: 0,
                            lastFrame: Number.MIN_SAFE_INTEGER,
                            },
                           currentGraphData: {...data.fetchedData}});
                    }else
                        this._failCommunication();
                });
            });
        }
    }

    _reloadSimulations = async () => {
        const {currentGraphData, simulationsMap} = this.state;
        const graph_id = currentGraphData.id;
        this.setState({...this.state, loading: true, loadingText: "Reloading Simulations..."}, async () =>
            {await executeFetch(endpoints.LOAD_GRAPH(graph_id)).then((data) => {
                if(data.err === undefined){
                    if(data.fetchedData.simulations !== undefined && data.fetchedData.simulations !== null)
                    data.fetchedData.simulations.forEach((d, ind) => {
                        simulationsMap.set(d.id, ind);
                    })
                    this.setState({...this.state, loading: false, simulationsMap: simulationsMap, 
                    currentGraphData: {...currentGraphData, simulations: data.fetchedData.simulations}});
                }else
                    this._failCommunication();
            })
        });
    }

    _requestNewResolution = async (newRes) => {
        const {matrixResolution} = this.state.visCfg;
        if(newRes !== matrixResolution) 
            this.setState({...this.state, loading: true, loadingText: "Waiting for Server..."}, 
                async () => {
                    await executeFetch(endpoints.UPDATE_RESOLUTION(newRes)).then((data) => {
                        if(data.err === undefined){
                            const {sector_quantization, simulation_matrices} = data.fetchedData;
                            let stateUpdate = this.state;
                            stateUpdate = {...stateUpdate, loading: false, visCfg: {...stateUpdate.visCfg, matrixResolution: newRes},
                                            currentGraphData: {...stateUpdate.currentGraphData, sectorQuantization: sector_quantization}};
                            if(stateUpdate.loadedSimulations.size > 0 && simulation_matrices !== null)
                                for(let sim of simulation_matrices)
                                    stateUpdate.loadedSimulations.set(sim.simulation_id, {...stateUpdate.loadedSimulations.get(sim.simulation_id), frames: sim.frames});
                            /*if(stateUpdate.visCfg.clickedSectors !== undefined && stateUpdate.visCfg.clickedSectors.length === 4){                                
                                stateUpdate = {...stateUpdate, visCfg};
                                this.setState(this._updateSectorDrawing(data.fetchedData.graph_sector, stateUpdate));
                            }else*/
                            this.setState(stateUpdate);
                            /*this.setState({...this.state, loading: false, visCfg: {...this.state.visCfg, matrixResolution: newRes},
                                currentGraphData: {...this.state.currentGraphData, sectorQuantization: data.fetchedData.sector_quantization}});*/
                            }
                        else
                            this._failCommunication();
                        });
                    });
    }

    _loadSimulations = async (sims_switches) => { //TODO: UNLOAD PREVIOUS SIMULATIONS
        const {currentGraphData, loadedSimulations, visCfg} = this.state;

        if(sims_switches === null)
            return;

        let leftoverSims = new Set();
        for(let i of loadedSimulations.keys()) leftoverSims.add(i);

        //let noOfSims = sims_switches.length;
        let simulationsToLoad = [];
        sims_switches.forEach((currentValue) => { 
            let sim_id = currentGraphData.simulations[currentValue].id
            //if(loadedSimulations.get(sim_id) === undefined) //ONLY LOAD NEW SIMULATIONS
            simulationsToLoad.push(sim_id); //LOAD ALL SIMULATIONS TO SYNC WITH REMOTE STATE
            leftoverSims.delete(sim_id);
            /*if(!value){
                sims_loading_accu[d] = loadedSimulations[d];
                current_sims++;
            }
            return value;*/
        });
        leftoverSims.forEach((value) => {
            loadedSimulations.delete(value);            
        });

        let tmpMaxActiveInSims = Number.MIN_SAFE_INTEGER;
        let tmpMaxSimsFrames = Number.MIN_SAFE_INTEGER;
        let currentFrame = visCfg.currentFrame;
        if(loadedSimulations.size > 0){
            loadedSimulations.forEach((val) => {
                tmpMaxSimsFrames = Math.max(tmpMaxSimsFrames, val.end_frame);
                val.frames.forEach((d) =>
                    tmpMaxActiveInSims = Math.max(tmpMaxActiveInSims, d.max_activated)
                );
            });
            if(visCfg.currentFrame > tmpMaxSimsFrames)
                currentFrame = tmpMaxSimsFrames;
        }

        this.setState({...this.state, loading: true, simulationsSideMenu: false, simulationsLength: loadedSimulations.size,
                                                loadedSimulations: loadedSimulations, loadingText: "Loading Simulations",
                                                maxSimsFrames: tmpMaxSimsFrames, maxActiveInSims: tmpMaxActiveInSims, visCfg: {...visCfg, currentFrame: currentFrame}}, 
            async () => {
                await executeFetch(endpoints.LOAD_SIMULATION_FRAMES(), simulationsToLoad).then((data) => {
                    if(data.err === undefined){
                        if(simulationsToLoad.length > 0)
                            this._updateSimulationsLoading(data.fetchedData);
                        else
                            this.setState({...this.state, loading: false, simulationsSideMenu: false, simulationsLength: loadedSimulations.size,
                                loadedSimulations: loadedSimulations, visCfg: {...visCfg, clickedSectors: null}}) 
                    }else
                        this._failCommunication();
                });
        });        
    }

    _updateSimulationsLoading = (data) => {
        const {maxActiveInSims, maxSimsFrames, visCfg, loadedSimulations} = this.state;
        //const {longestSimulation} = visCfg;
        //const {simulations, vertices} = currentGraphData;

        //sims_loading_accu[sim_id] = data;
        var tmpMaxSimsFrames = Math.max(maxSimsFrames, Number.MIN_SAFE_INTEGER);
        //var tmpLongestSimulation = Math.max(maxSimsFrames, Number.MIN_SAFE_INTEGER;
        var tmpMaxActiveInSims = Math.max(maxActiveInSims, Number.MIN_SAFE_INTEGER);
        //current_sims++;
        //if(current_sims === simulationsLength/* || next_simulation === Object.entries(currentGraphData.simulations).length*/){
        for(let i in data){
            let curr = data[i];
            i = parseInt(i); //INSERT HERE THE CODE FOR STRATEGY IDENTIFICATION
            //let cumulative = 0;
            tmpMaxSimsFrames = Math.max(tmpMaxSimsFrames, curr.end_frame);
            curr.frames.forEach((d) => {
                tmpMaxActiveInSims = Math.max(d.max_activated, tmpMaxActiveInSims);
            });
            //tmpLongestSimulation = Math.max(curr.frames.length, tmpLongestSimulation);
        }
        for(let sim of data)
            loadedSimulations.set(sim.simulation_id, {...sim, viewOptions: {peekSeeds: false, localCoordinates: false, showActiveEdges: true, showAllEdges: false, showLowInfection: false, showHighInfection: false}});

        this.setState({...this.state, 
            maxSimsFrames: tmpMaxSimsFrames, 
            maxActiveInSims: tmpMaxActiveInSims,            
            visCfg: {...visCfg, lastFrame: tmpMaxSimsFrames},//, currentFrame: 0}, 
            loading: false,  
            loadedSimulations: loadedSimulations}, () => {
                if(visCfg.clickedSectors !== undefined && visCfg.clickedSectors !== null){
                    this._getSectorDrawing(visCfg.clickedSectors);
                }
            });
    }

    _toggleDrawer = (type, open) => (event) => {
        const {graphsSideMenu, simulationsSideMenu} = this.state;
        switch(type){
            case "graphs": if(open !== graphsSideMenu) this.setState({...this.state, graphsSideMenu: open }); break;
            default: if(open !== simulationsSideMenu) this.setState({...this.state, simulationsSideMenu: open }); break;
        }
    }    

    _updateVisCfg = (newVisCfg) => {
        this.setState({...this.state, visCfg: {...newVisCfg}});
    }
    
    _getSectorDrawing = (ids) => {
        const {visCfg} = this.state;
        if(ids.snappedCoords !== undefined && ids.start_x !== undefined && ids.start_y !== undefined && ids.span_x !== undefined && ids.span_y !== undefined)
            this.setState({...this.state, loading: true, loadingText: "Waiting for Server...", visCfg: {...visCfg, clickedSectors: ids}}, async () => {
                await executeFetch(endpoints.GET_SECTORS_DRAWING(), ids)
                .then((data) => {
                    if(data.err === undefined){  
                        //console.log("Response received");                
                        this.setState(this._updateSectorDrawing(data.fetchedData, this.state));
                    }else
                        this._failCommunication();
                });
            });        
    }

    _updateSectorDrawing = (data, state) => {
        const {currentGraphData, loadedSimulations} = state;
        for(let sim of data.simulation_chunks){
            //let verticesMap = new Map();
            /*for(let v of sim.vertices)
                verticesMap.set(v.id, {id: v.id, coords: v.coords, global_coords: v.global_coords});*/
            loadedSimulations.get(sim.simulation).graphChunk = {
                edges: sim.edges,
                vertices: sim.vertices,
                //verticesMap: verticesMap,
                boundingBox: sim.bounding_box
            };
        }
        
        let structuralVertices = new Map();
        data.structural_vertices.forEach((v) => 
            structuralVertices.set(v.id, {id: v.id, global_coords: v.coords, degree: v.degree, vertexSector: v.vertex_sector}));
        return {...state, loading: false,
            currentGraphData: {...currentGraphData, structuralVertices: structuralVertices, 
                structuralEdges: data.structural_edges, currentBoundingBox: data.sector_bounding_box},
                loadedSimulations: loadedSimulations};
    }

    _getBoundingClient = () => {
        return d3.select("#gridRoot").node().getBoundingClientRect();
    }

    _onSimulationPanelClose = (data = undefined) => {
        const {seedsMods, selectedSimulation, currentGraphData} = this.state;
        const {id} = currentGraphData;
        if(data === undefined)
            this.setState({...this.state, simulationModal: false});
        else{
            this.setState({...this.state, loading: true, loadingText: "Contacting Server", simulationModal: false}, 
            async () => {
                let simulationData = {};
                simulationData.model = data.selectedModel;
                simulationData.selectionStrategy = data.selectedStrategy;
                
                if(data.randomSeeds !== undefined){
                    simulationData.randomSeeds = parseInt(data.randomSeeds);                
                }else{
                    simulationData.random_seeds = -1;
                    let seedsToAdd = [], seedsToRemove = [];
                    for(let s of seedsMods)
                        if(s.type > 0)
                            seedsToAdd = seedsToAdd.concat(s.elements.map(d => d.id));
                        else
                            seedsToRemove = seedsToRemove.concat(s.elements.map(d => d.id));
                    simulationData.seedsToRemove = seedsToRemove;
                    simulationData.seedsToAdd = seedsToAdd;
                    simulationData.donorSimulation = selectedSimulation;
                }

                await executeFetch(endpoints.REQUEST_SIMULATION(id), simulationData).then((data) => {
                    if(data.err === undefined)
                        this.setState({...this.state, loading: false, messageModal: true, messageModalText: "Requested Simulation is Running", messageModalTitle: "Request Submitted"});
                    else
                        this._failCommunication();
                });
            });
        }
    }

    _newSeedMod = (data = undefined) => {
        const {seedsMods} = this.state;        
        if(data !== undefined){
            seedsMods.push(data);
            this.setState({...this.state, seedsMods: seedsMods});
        }

    }

    _failCommunication = (err) => {
        this.setState({...this.state, loading: false, messageModal: true, messageModalTitle: "Something went wrong", messageModalText: err === undefined || err === null ? "Please try the last operation again." : err});
    } 

    render(){
        const {classes} = this.props;
        const {firstRun, loading, simulationModal, availableMaximizations, maxSimsFrames, loadingText, selectedSimulationNoOfSeeds,
              loadedSimulations, currentGraphData, availableGraphs, messageModal, messageModalTitle,
              graphsSideMenu, simulationsSideMenu, simulationsMap, messageModalText, hideTopBar,
              maxActiveInSims, visCfg, coordinatedHoveredVertex, selectedSimulation, seedsMods} = this.state; 
        const {matrixResolution, firstFrame, currentFrame, clickedSectors} = visCfg;
        const {name, simulations} = currentGraphData;        

        let noOfSims= loadedSimulations.size;      

        /*
          {hover.hoveredObject && (
                <div
                    className={classes.hoverLabel}
                    style={{ transform: `translate(${hover.x}px, ${hover.y}px)` }}
                >
                    {hover.label}
                </div>
            )}
        */

        let drawer = graphsSideMenu ? 
            (<GraphSelector
                menuData={availableGraphs}
                onUpdatedSelection={(data) => this._loadGraph(data)}
                //toggleDrawer={this._toggleDrawer}
                open={graphsSideMenu}
            />) : (simulationsSideMenu ? (
                <SimulationsSelector
                    vertexCount={currentGraphData.vertexCount}    
                    menuData={simulations}
                    loadedSimulations={loadedSimulations}
                    onUpdatedSelection={(sims) => {if(sims !== null) this._loadSimulations(sims); else this.setState({...this.state, simulationsSideMenu: false});}}
                    //toggleDrawer={this._toggleDrawer}
                    open={simulationsSideMenu}
            />
            ) : "");
        
        let diagrams = [];        
        let simulationInfos = [];
        if(noOfSims > 0){
            for(let j of loadedSimulations.keys()){
                j = parseInt(j);
                let simulation = loadedSimulations.get(j);
                //let simulationData = simulations[simulationsMap.get(simulation.simulation_id)];
                let currentValidFrame = currentFrame < simulation.frames.length ? currentFrame : simulation.frames.length - 1;
                diagrams.push(
                        <Grid key={`grid_${j}`} variant="outlined" item xs={/*12/noOfSims*/ 6}>
                                <SimulationSmallMultiple 
                                    key={j}
                                    simId={j}
                                    bounds={currentGraphData.bounds}
                                    sectorBounds={currentGraphData.currentBoundingBox}
                                    structuralEdges={currentGraphData.structuralEdges} 
                                    structuralVertices={currentGraphData.structuralVertices} 
                                    resolution={matrixResolution}
                                    simulation={simulation}
                                    maxActiveInSims={maxActiveInSims}
                                    currentFrame={currentValidFrame}
                                    maxSimsFrames={maxSimsFrames}
                                    clickedSectors={clickedSectors}
                                    onNewSeedMod={(data) => this._newSeedMod(data)}
                                    modifyHoveredVertex={(vertex) => this.setState({...this.state, coordinatedHoveredVertex: vertex})}
                                    coordinatedHoveredVertex={coordinatedHoveredVertex}                                    
                                    cellValue={(d) => d.verticesAggregator === 0 ? 0 : (d.edgesAggregator/d.verticesAggregator).toFixed(2)}
                                />                            
                        </Grid> 
                );
            }

            loadedSimulations.forEach((curr) => simulationInfos.push(currentGraphData.simulations[simulationsMap.get(curr.simulation_id)]));
     
        } 

        return(
            <div style={{overflowX: "hidden"}} onContextMenu={event => event.preventDefault()}>
                <LoadingAnimation 
                    active={loading}
                    loading_label={loadingText}
                />
                <MessageModalPanel 
                    active={messageModal}
                    message={messageModalText}
                    title={messageModalTitle}
                    onClose={() => this.setState({...this.state, messageModal: false})}
                />
                <MessageModalPanel 
                    active={firstRun && !loading}
                    title={"Welcome to VAIM!"}
                    message={(
                        <div>
                            <p>Welcome to VAIM!</p>
                            <p>This code is supplemental material to the <a href="https://www.computer.org/csdl/journal/tg/5555/01/09829321/1EYxoEPe9eU" target="_blank">IEEE TVCG</a> paper.</p>                            
                            <p>Please  check <a href="https://github.com/EngAAlex/VAIM" target="_blank">VAIM project GitHub page</a> for more information, supplemental video with usage description, and more downloadable material.</p>
                            <p>Thank you for your time exploring VAIM!</p>
                            <p style={{fontStyle: "italic"}}>Yours sincerely,<br />VAIM Authors</p>
                            <p>DISCLAIMER: The performance of the system depends on the resolution of your monitor 
                                (recommended 1080p), your computer specifications, and network speed. The larger the graphs you load, 
                                and the larger portions of them you load into the node-link view might cause longer waiting times. Finally, 
                                please allow for a few minutes when computing simulations with many seeds and/or on larger graphs. If you find the
                                dashboard too "crowded" (possibly due to a low dpi screen) try to lower the zoom of the window until you see
                                all the elements properly arranged - and reload the page.
                            </p>
                        </div>
                    )}
                    onClose={() => this.setState({...this.state, firstRun: false})}
                />                  
                <SimulationModalPanel
                    active={simulationModal}
                    noOfVertices={currentGraphData.vertexCount}
                    onPanelClose={(data) => this._onSimulationPanelClose(data)}
                    availableModels={AVAILABLE_MODELS}
                    availableStrategies={availableMaximizations}
                    seedsMods={seedsMods}
                    onSeedsModsReset={() => this.setState({...this.state, seedsMods: []})}
                    currentSimulation={loadedSimulations.get(selectedSimulation)}
                    currentSimulationNoOfSeeds={selectedSimulationNoOfSeeds}
                />
                <AppBar style={{display: hideTopBar ? "none" : "block"}} /*className={classes.appBar} classes={APPBAR_STYLES}*/ position="static">
                    <Toolbar variant={"dense"}>
                        <Typography variant="h6" className={classes.title}>
                            VAIM Diffusion Simulation Explorer {name === undefined ? "" : `| Opened ${name}`}
                        </Typography>
                        <IconButton aria-label="Select graph" color="inherit" onClick={this._toggleDrawer("graphs", !graphsSideMenu)}>
                            <Badge badgeContent={availableGraphs.length} color="secondary">
                                <AcUnitIcon />Graphs
                            </Badge>
                        </IconButton>
                        <IconButton aria-label="Select Simulations" color="inherit" onClick={this._toggleDrawer("sims", !simulationsSideMenu)} disabled={simulations === null || simulations === undefined || simulations.length === 0}>
                            <Badge badgeContent={simulations !== null && simulations !== undefined ? simulations.length : undefined} color="secondary">
                                <BugReportIcon />Simulations
                            </Badge>
                        </IconButton>
                        <IconButton aria-label="Reload Simulations" color="inherit" onClick={() => this._reloadSimulations()} disabled={Object.keys(currentGraphData).length === 0}>
                                <CachedIcon />
                        </IconButton>                        
                        <IconButton aria-label="Simulate New" color="inherit" onClick={() => this.setState({...this.state, simulationModal: true})} disabled={Object.keys(currentGraphData).length === 0}>                            
                            <Badge badgeContent={availableGraphs.length > 0 && seedsMods.length > 0 ? seedsMods.length : undefined} color="secondary">
                                <LeakAddIcon />Compute Simulation                            
                            </Badge>
                        </IconButton>
                        <IconButton style={{marginLeft: "19%", fontSize: "large"}} aria-label="Hide Bar" color="inherit" onClick={() => this.setState({...this.state, hideTopBar: true})}>                            
                            <KeyboardArrowUpIcon />                          
                        </IconButton>
                        <Typography className={classes.version}>Version: {CURR_VERSION}</Typography>                    
                    </Toolbar>
                </AppBar>
                <IconButton style={{display: hideTopBar ? "block" : "none"}} className={classes.showTopButton} aria-label="Hide Bar" color="inherit" onClick={() => this.setState({...this.state, hideTopBar: false})}>                            
                            <KeyboardArrowDownIcon />                            
                </IconButton>
                {drawer}
                <Grid id={'gridRoot'} container className={classes.gridRoot} spacing={0}>
                    <Grid variant="outlined" key={"sec1"} item /*xs={2}*/ style={{width: "14%"}}>
                        <Grid key={"graph/infoArea"} item xs={12}>
                            <Paper elevation={3} square className={classes.paperLabel}>
                                <Typography className={classes.sectionTitle}>Graph Info</Typography>     
                            </Paper>   
                            <div className={classes.gridMatrixRow}>                        
                                {Object.entries(currentGraphData).length === 0 ?
                                    <Typography className={classes.centeredText}>Please select a graph from above</Typography> :
                                                    <div className={classes.simTableContainer}>
                                                    <table className={classes.simTable}>
                                                        <thead>
                                                            <tr>
                                                                <th className={classes.simTableHead}>|V|</th><th className={classes.simTableHead}>|E|</th>
                                                            </tr>                            
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td style={{width: "50%", textAlign: "center"}} className={classes.simTableCell}>
                                                                    <Typography>{getSuffixString(currentGraphData.vertexCount)}</Typography>
                                                                </td>
                                                                <td style={{width: "50%", textAlign: "center"}} className={classes.simTableCell}>
                                                                <   Typography>{getSuffixString(currentGraphData.edges)}</Typography>
                                                                </td>                            
                                                            </tr>  
                                                        </tbody>                        
                                                    </table>
                                                </div>
                                }
                            </div>
                        </Grid>                        
                        <Grid className={classes.gridMatrixRow} item xs={12}>
                            {Object.entries(currentGraphData).length === 0 ?
                                        "" :
                                <Paper elevation={3} square className={classes.paperLabel}>
                                <Typography className={classes.sectionTitle}>Graph Density Matrix</Typography>
                            </Paper>}   
                            <div className={classes.gridMatrixRow}>                                                     
                                <div id="adjMatrixArea" className={classes.adjMatrixContainer}>
                                    {Object.entries(currentGraphData).length === 0 ?
                                        "" :
                                        <MatrixComponent
                                        areaSelector="#adjMatrixArea"
                                        bounds={currentGraphData.bounds}
                                        matrixData={currentGraphData.sectorQuantization}
                                        resolution={matrixResolution}
                                        onCellBrushed={(ids) => 
                                            this._getSectorDrawing(ids)
                                        }
                                        showInfection={false}
                                        colorScheme={d3.interpolatePurples}
                                        cellValue={(d) => d.verticesAggregator}
                                        x={(d) => d.x}
                                        y={(d) => d.y}
                                        activateBrushing={noOfSims > 0}
                                        clickedSectors={clickedSectors}
                                    />}
                                </div>
                                <ResolutionSliderComponent 
                                    enabled={Object.entries(currentGraphData).length > 0}                                    
                                    resolutionsArray={RESOLUTIONS}
                                    defaultResolution={DEFAULT_MATRIXRES}
                                    onResolutionChanged={(newRes) => this._requestNewResolution(RESOLUTIONS[newRes])}
                                />
                            </div>
                        </Grid>                       
                        <Grid className={classes.gridMatrixRow}  key={"graph/simControlArea"} item xs={12}>
                                <div className={classes.gridMatrixRow}>                        
                                    <SimulationControlComponent 
                                        simulationsData={simulationInfos}
                                        simulationsContainer={loadedSimulations}
                                        currentFrame={currentFrame}
                                        firstFrame={firstFrame}
                                        lastFrame={maxSimsFrames}
                                        ff={() => 
                                            this.setState({...this.state, visCfg: {...visCfg, currentFrame: currentFrame + 1, forwards: true}}
                                                //,() => this.setState({...this.state, visCfg: {...visCfg, frameChanged: false}})
                                                )
                                            }
                                        rw={() => 
                                            this.setState({...this.state, visCfg: {...visCfg, currentFrame: currentFrame - 1, forwards: false}}
                                                //,() => this.setState({...this.state, visCfg: {...visCfg, frameChanged: false}})
                                                )
                                            }
                                        play={() => {console.log("play clicked")}}
                                        setFrame={(newFrame) => {console.log(newFrame); this.setState({...this.state, visCfg: {...visCfg, currentFrame: newFrame}})}}                    
                                        updateViewOption={(id, options) => {                                            
                                            let oriOptions = loadedSimulations.get(id).viewOptions;
                                            oriOptions = {...oriOptions, ...options};
                                            loadedSimulations.get(id).viewOptions = oriOptions;
                                            this.setState({...this.state, loadedSimulations: loadedSimulations});
                                            }
                                        }
                                        onChangedSelectedSimulation={(id, seeds) => this.setState({...this.state, selectedSimulation: id, selectedSimulationNoOfSeeds: seeds})}
                                    />
                                </div>
                        </Grid>
                    </Grid>
                    <Grid style={{height: "100%", width: "86%"}} variant="outlined" key={"graph/diagramArea"} item /*xs={10}*/>
                        <Grid container spacing={0}>
                            {diagrams}
                        </Grid>
                    </Grid>
                </Grid>
            </div>
        );
    }

}

const VaimWindow = withStyles(VAIN_STYLES)(Vaim);

export default VaimWindow;