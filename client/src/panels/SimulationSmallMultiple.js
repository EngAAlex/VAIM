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

import Paper from '@material-ui/core/Paper';
import Typography from '@material-ui/core/Typography';
import {withStyles} from "@material-ui/core/styles";
import * as d3 from 'd3';

import MatrixComponent from '../diagrams/matrix.js';
import AreaChartComponent from '../diagrams/areachart.js';
import BoxWhiskerComponent from '../diagrams/boxwhisker.js';
import GraphComponent from '../diagrams/graphChunk.js';
import {SeedsModalPanel} from './modals.js'

import {makeId} from '../utils/utils.js';

const SMALL_MULTIPLE_STYLES = {
    root: {
        minWidth: "125px",
        padding: "2px",
    },
    infoSec: {
        padding: "3px",
        width: "100%",
        display: "inline-block"
        //height: "27%"
    },
    areaChartContainer: {
        width: "49%",
        display: "inline-block",
        //height: "100%"
    },
    gridFull:{
        height: "100%",
        //borderRight: "solid 2px gray",
        //paddingTop: "6px",
        //paddingRight: "6px"
    },
    graphSec: {
        //border: "solid 1px black",
        width: "41%",
        display: "inline-block",
        //height: "61%",
        //borderRadius: "4px",
        position: "relative",
        float: "right",
        right: "26px"
    },
    matrixSec: {
        width: "41%",
        display: "inline-block",
        position: "relative",
        height: "100%",
        left: "80px"
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
}

class SimulationSmallMultiple extends Component {

    constructor(props){
        super(props);
        this.state = {
            divId: makeId(6), 
            efficiencyModal: {showModal: false}
        };
    }

    componentDidUpdate(){

    }
    
    componentDidMount(){
        
    }

    _onCellClicked = (cell) => {
        const {structuralVertices, simulation} = this.props;
        const {divId} = this.state;

        var cellid = cell.sectorId;
        if(cellid === undefined)
            cellid = cell.currentTarget.getAttribute("sectorId");
            
        if(simulation.graphChunk === undefined || cell.verticesAggregator === 0)
            return;

        let efficiency =  parseFloat(d3.select(`#small_mult-${divId}-matrix rect.matrixRect-${cellid}`).attr("cumulative_eff"));

        let domCircles = d3.selectAll(`#small_mult-${divId} circle.vsector${cellid}`);
        var circles = {seeds: [], nonseeds: []};
        //if(efficiency < 0.5){
            domCircles.each((d) => {
                if(d.frames === null || d.frames.cumulative === null || d.frames.cumulative[0] === 0)
                    circles.nonseeds.push({id: d.id, frames: d.frames.cumulative});
                else if(d.frames !== null && d.frames.cumulative !== null && d.frames.cumulative[0] > 0)
                    circles.seeds.push({id: d.id, frames: d.frames.cumulative});
            });

            circles.nonseeds.sort((de, te) => {
                let d = structuralVertices.get(de.id);
                let t = structuralVertices.get(te.id);
                if(d.degree < t.degree)
                    return 1;
                else if(d.degree > t.degree)
                    return -1;
                return 0;
            });
        //}else{
        /*    domCircles.each((d) => {
                if(d.frames !== null && d.frames.cumulative !== null && d.frames.cumulative[0] > 0)
                    circles.push({id: d.id, frames: d.frames.cumulative});
                });*/
            circles.seeds.sort((de, te) => {
                let d = structuralVertices.get(de.id);
                let t = structuralVertices.get(te.id);
                if(d.degree < t.degree)
                    return -1;
                else if(d.degree > t.degree)
                    return 1;
                return 0;
            });        
        //}
        this.setState({...this.state, efficiencyModal: {showModal: true, efficiency: efficiency, nodesList: circles, sectorId: cellid}});
    }

    render(){
        const {simId, classes, structuralVertices, structuralEdges, simulation, currentFrame, 
               maxActiveInSims, resolution, clickedSectors, cellValue, onNewSeedMod,
               maxSimsFrames, bounds, coordinatedHoveredVertex, modifyHoveredVertex, sectorBounds} = this.props;
        const {showAllEdges, showActiveEdges, peekSeeds, localCoordinates, showLowInfection, showHighInfection} = simulation.viewOptions;
        const {divId, hoveredVertex, efficiencyModal} = this.state;      
        const {showModal, efficiency, nodesList, sectorId} = efficiencyModal;

        let fixedHoveredVertex = hoveredVertex === undefined ? 
        (coordinatedHoveredVertex === undefined ? coordinatedHoveredVertex : d3.select(`#small_mult-${divId}-graphComponent`).select("circle.node-" + coordinatedHoveredVertex.id).data()[0])
        : hoveredVertex;

        if(fixedHoveredVertex !== undefined && fixedHoveredVertex.frames !== null && fixedHoveredVertex.frames.cumulative !== null){
            if(fixedHoveredVertex.frames.cumulative.length < simulation.end_frame + 1){
                let cumulativeMaxValue = fixedHoveredVertex.frames.cumulative[fixedHoveredVertex.frames.cumulative.length - 1];
                for(let s = fixedHoveredVertex.frames.cumulative.length; s <= simulation.end_frame; s++)
                    fixedHoveredVertex.frames.cumulative.push(cumulativeMaxValue);
            }
        }

        return(<div id={`small_mult-${divId}`} className={classes.gridFull}>
                <SeedsModalPanel 
                    active={showModal}
                    nodesList={nodesList}
                    efficiency={efficiency}
                    structuralVertices={structuralVertices}
                    onClose={(data) => {
                        if(data !== undefined && data.value > 0)
                            onNewSeedMod({...data, elements: (data.type > 0 ? nodesList.nonseeds : nodesList.seeds).slice(0, data.value), sectorId: sectorId});
                        this.setState({...this.state, efficiencyModal: {showModal: false}});                                                    
                        }
                    }   
                />
                <Paper elevation={3} square className={classes.paperLabel}>
                    <Typography className={classes.sectionTitle}>Simulation {simId}</Typography>
                </Paper>
                <div className={classes.infoSec}>
                    <div id={`small_mult-${divId}-boxwisk`} className={classes.areaChartContainer}>
                        <BoxWhiskerComponent 
                            areaSelector={`#small_mult-${divId}-boxwisk`}
                            currentFrame={currentFrame}
                            simulation={simulation}
                            maxActive={maxActiveInSims}
                            maxSimsFrames={maxSimsFrames}
                        />
                    </div>          
                    <div id={`small_mult-${divId}-area`} className={classes.areaChartContainer}>
                        <AreaChartComponent 
                            areaSelector={`#small_mult-${divId}-area`}
                            simulation={simulation}
                            hoveredVertex={fixedHoveredVertex}
                            currentFrame={currentFrame}
                            maxSimsFrames={maxSimsFrames}
                        />
                    </div>       
                    <div id={`small_mult-${divId}-graphComponent`} className={classes.graphSec}>
                        {simulation.graphChunk === undefined ? "" : 
                            <GraphComponent 
                                simId={simId}
                                areaSelector={`#small_mult-${divId}-graphComponent`}
                                chunk={simulation.graphChunk}
                                structuralVertices={structuralVertices}
                                structuralEdges={structuralEdges}
                                sectorBounds={sectorBounds}
                                graphBounds={bounds}
                                resolution={resolution}
                                currentFrame={currentFrame}
                                showAllEdges={showAllEdges}
                                showActiveEdges={showActiveEdges}
                                localCoordinates={localCoordinates}
                                onCellClicked={(d) => this._onCellClicked(d)}
                                //vertices={vertices}
                                clickedSectors={clickedSectors}
                                cellValue={(d) => d.verticesAggregator === 0 ? 0 :(d.edgesAggregator/d.verticesAggregator).toFixed(2)}
                                onVertexHover={(vertex) => this.setState({...this.state, hoveredVertex: vertex}, () => modifyHoveredVertex(vertex))}
                            />
                        }     
                    </div>                                              
                    <div id={`small_mult-${divId}-matrix`} className={classes.matrixSec}>
                        <MatrixComponent
                            activateBrushing={false}
                            areaSelector={`#small_mult-${divId}-matrix`}
                            //bounds={bounds}
                            resolution={resolution}
                            //vertices={vertices}
                            matrixData={simulation.frames[currentFrame].matrix}
                            seedsDist={simulation.frames[0].matrix}
                            fullDist={simulation.frames[simulation.frames.length-1].matrix}
                            //givenFrame={drawnFrame}
                            peekSeeds={peekSeeds}
                            currentFrame={currentFrame}
                            showInfection={true}
                            showLowInfection={showLowInfection}
                            //showMedInfection={showMedInfection}
                            onCellClicked={(d) => console.log(d) }//this._onCellClicked(d)}
                            showHighInfection={showHighInfection}
                            simId={simId}
                            colorScheme={d3.interpolateYlOrRd}
                            cellValue={(d) => cellValue(d)}
                            x={(d) => d.x}
                            y={(d) => d.y}
                            clickedSectors={clickedSectors}
                        />
                    </div>
                </div>
           
            </div>                
        );

    }

}

const SimulationSmallMultipleComponent = withStyles(SMALL_MULTIPLE_STYLES)(SimulationSmallMultiple);

export default SimulationSmallMultipleComponent;