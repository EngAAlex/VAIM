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

import React from 'react';
import * as d3 from 'd3';
import { withStyles } from '@material-ui/core';
import ContinuosLegendComponent, {VERTICAL_ORIENTATION, LEGEND_AREA_MINOR_AXIS_EXTENT, SNIP_RIGHT} from '../panels/continousLegend.js'

//const PADDING_X = 0;
//const PADDING_Y = 0;

const GRAPH_STYLES = {
    legendContainer: {
        display: "inline-block",
        position: "relative",
        float: "left",
        width: `${LEGEND_AREA_MINOR_AXIS_EXTENT}px`,
        height: "100%"
    }
}

class GraphWindow extends React.Component {

    constructor(props){
        super(props)
        this.state = {...this.state, 
            structuralEdgesEnabled: false,
            activeEdgesEnabled: true,
            stateFrame: -1,
            activeSetFrame: 0,
            width: Number.MIN_SAFE_INTEGER,
            height: Number.MIN_SAFE_INTEGER,
            storedLocalCoordinates: false,
            structuralEdgeOpacity: d3.scaleSequential([0,1]).range([0.05, 0.45])
        };
    }

    ACTIVATED_EDGE_COLOR = "darkorange";
    ACTIVATED_EDGE_OPACITY = 0.7;
    ACTIVATED_STROKE_WIDTH = 2;

    INACTIVE_VERTEX_COLOR = "black";
    INACTIVE_VERTEX_OPACITY = 0.25;
    INACTIVE_VERTEX_GIRTH = 2;

    ACTIVE_VERTEX_GIRTH = 3;
    ACTIVE_VERTEX_COLOR = "purple"
    ACTIVE_VERTEX_OPACITY = 1;

    SEED_COLOR = "blue";
    SEED_GIRTH = 4;
    SEED_OPACITY = 0.7;

    HIGHLIGHTED_VERTEX_COLOR = "lime"
    HIGHLIGHTED_VERTEX_OPACITY = 0.9;
    HIGHLIGHTED_VERTEX_GIRTH = 6;

    STRUCTURAL_EDGE_OPACITY = 0.45;
    STRUCTURAL_STROKE_WIDTH = 1;
    STRUCTRUAL_EDGE_COLOR = "grey";

    componentDidUpdate(prevProps){
        const {chunk, areaSelector, currentFrame, graphBounds, clickedSectors, showAllEdges, showActiveEdges, resolution} = this.props;
        const {storedWidth, structuralEdgesEnabled, activeEdgesEnabled, stateFrame} = this.state;

        let area = d3.select(`${areaSelector}`);
        let width = area.node() === null ? 490 : area.node().getBoundingClientRect().width - LEGEND_AREA_MINOR_AXIS_EXTENT - 55;
        //width += 50;
        //let height = width;
        //let height = area.node() === null ? 490 : area.node().getBoundingClientRect().height;

        //let svg = 
        //    area.select("svg");

        let stateUpdate = {};
        let changeDetected = false;    

        if(prevProps.chunk !== chunk ||
            prevProps.graphBounds !== graphBounds || 
            prevProps.resolution !== resolution ||
            Math.round(storedWidth) !== Math.round(width)){                
            let globalBBoxWidth = (graphBounds.maxX - graphBounds.minX);
            let globalBBoxHeight = (graphBounds.maxY - graphBounds.minY);
            let cellWidth = globalBBoxWidth > globalBBoxHeight ? globalBBoxWidth/resolution : globalBBoxHeight/resolution;
            //let sectorBBoxWidth = sectorBounds.maxX - sectorBounds.minX;
            //let sectorBBoxHeight = sectorBounds.maxY - sectorBounds.minY;
            //let sectorBox = sectorBBoxWidth > sectorBBoxHeight ? sectorBBoxWidth : sectorBBoxHeight;
            this.setState({
                ...this.state,
                storedWidth: width,
                storedHeight: width,
                xRatio: (width)/(cellWidth*clickedSectors.span_x),
                yRatio: (width)/(cellWidth*clickedSectors.span_y),
                cellWidth: cellWidth,
            }, () => {
            this._vertexUpdate();
            if(structuralEdgesEnabled)
                this._updateStructureLinks();
            if(activeEdgesEnabled)
                this._updateActiveEdges(stateFrame);
            });
            return;
        } //CONDITION THAT UPDATES EDGES/VERTICES CHUNK        

        if(showActiveEdges !== activeEdgesEnabled){
            if(!showActiveEdges)
                this._toggleActiveEdges();
            else
                this._updateActiveEdges(currentFrame);
            stateUpdate = {...stateUpdate, activeEdgesEnabled: showActiveEdges};
            changeDetected = true;                
        }

        if(showAllEdges !== structuralEdgesEnabled){
            changeDetected = true;
            stateUpdate = {...stateUpdate, structuralEdgesEnabled: showAllEdges};
            if(showAllEdges)
                this._updateStructureLinks();
            else
                this._disableStructureLinks();
        }

        if(currentFrame !== stateFrame){
            //this._updateActiveVertices();
            this._vertexUpdate();
            if(activeEdgesEnabled)
                this._updateActiveEdges(currentFrame);
            //this._activeEdgesReset(chunk.edges[currentFrame]);
            stateUpdate = {...stateUpdate, stateFrame: currentFrame};
            changeDetected = true;
        }
        
        if(changeDetected)
            this.setState({...this.state, ...stateUpdate});
            

    }

    componentDidMount(){
        this._generateDOMElement();
    }

    _generateDOMElement = () => {
        const {areaSelector, graphBounds, resolution, clickedSectors} = this.props;
        //const {storedLocalCoordinates} = this.state;
        
        let area = d3.select(`${areaSelector}`);
        let width = area.node() === null ? 490 : area.node().getBoundingClientRect().width - LEGEND_AREA_MINOR_AXIS_EXTENT - 65;
        //width += 50; ## uncomment to enlarge node-link view
        
        //let height = width;
        //let height = area.node() === null ? 490 : area.node().getBoundingClientRect().height;
        //let bBox = storedLocalCoordinates ? chunk.boundingBox : graphBounds;
        let globalBBoxWidth = (graphBounds.maxX - graphBounds.minX);
        let globalBBoxHeight = (graphBounds.maxY - graphBounds.minY);
        let cellWidth = globalBBoxWidth > globalBBoxHeight ? globalBBoxWidth/resolution : globalBBoxHeight/resolution;
        //let sectorBBoxWidth = sectorBounds.maxX - sectorBounds.minX;
        //let sectorBBoxHeight = sectorBounds.maxY - sectorBounds.minY;
        //let sectorBox = sectorBBoxWidth > sectorBBoxHeight ? sectorBBoxWidth : sectorBBoxHeight;
        this.setState({
            ...this.state,
            storedWidth: width,
            storedHeight: width,
            xRatio: (width)/(cellWidth*clickedSectors.span_x),
            yRatio: (width)/(cellWidth*clickedSectors.span_y),
            cellWidth: cellWidth,
            edgeInfectionScale: d3.scaleSequential(d3.interpolateReds),
            vertexBrightnessScale: d3.scaleSequential(d3.interpolatePurples)
        }, () => {
            d3.select(`${areaSelector}`)
                .select("svg")
                .attr("width", this.state.storedWidth)
                .attr("height", this.state.storedHeight)
            this._vertexUpdate();
        });            

    }

    _updateDrawingGrid = () => {
        const {storedWidth, storedHeight} = this.state;
        const {areaSelector, clickedSectors, cellValue, onCellClicked} = this.props;
        const {start_x, start_y, span_x, span_y} = clickedSectors;

        let xSpace = storedWidth/span_x;
        let ySpace = storedHeight/span_y;
        let drawingArea = d3.select(`${areaSelector} svg g.grid`);
        drawingArea.selectAll("rect.grid").remove()
        for(let i=0; i<span_y; i++)
            for(let t=0; t<span_x; t++)
                drawingArea.append("rect")
                .attr("class",`grid cell-${t+start_x}-${i+start_y}`)
                .attr("sectorId", `${t+start_x}-${i+start_y}`)
                .attr("width", xSpace)
                .attr("height", ySpace)
                .attr("x", t*xSpace)
                .attr("y", i*ySpace)
                .style("stroke", "lightgrey")
                .style("stroke-width", "1px")
                .style("fill","transparent")
                .style("opacity", 0.5)
                .on('click', (d) => onCellClicked(d))
                .on('mouseenter', (d, event) => 
                {

                    let sectorId = d.currentTarget.getAttribute("sectorId")

                    let rects = d3.selectAll(`rect.matrixRect-${sectorId}, rect.cell-${sectorId}`);
                    rects
                         .classed("highlightedCell", true);
                        /*.transition().duration(500).style('stroke', 'black')
                        .style('stroke-width', '5px')
                        .style('stroke-opacity', 1);*/
                    //var rectNode = d3.select(`rect.matrixRect-${sectorId}`);
                    //var cellWidth = rectNode.node().getBoundingClientRect().width;
                    rects.each((d, ind) => {
                        let nodeObj = d3.select(rects.nodes()[ind]).node();
                        let node = nodeObj.getBoundingClientRect();
                        let side = "left";
                        let sideMargin = node.right;
                        if(sideMargin > window.innerWidth*0.9){
                            side = "right";
                            sideMargin =  window.innerWidth - node.left; 
                        }             

                        let seeds = nodeObj.getAttribute("seeds");
           
                        if(d !== undefined)
                            d3.select("body").append("div")
                                .attr("class", "tooltips")
                                .style("opacity", 0)
                                .style("position", "absolute")
                                .style('top', (node.top /*+ cellWidth*/)+'px')
                                .style(side, sideMargin +'px')//.style('left', (node.left + cellWidth < node.width ? node.left + cellWidth : node.left - 3*cellWidth) +'px')
                                .html(
                                    ind === 0 ? `${d.sectorId}<br/>Nodes in area: ${d.verticesAggregator} <br/>` : 
                                    "Avg. Activation Rate: " + cellValue(d) + "<br/>" +
                                    (parseInt(seeds) > 0 ? "Present " + seeds + " seeds" : "")
                                    )                  
                                .transition().duration(300)
                                .style("opacity", 1);
                    });        
                }).on('mouseleave', (event, d) => {
                    d3.selectAll(`rect.highlightedCell`)
                    .classed("highlightedCell", false);
        
                    d3.selectAll('div.tooltips')
                        .transition().duration(200)
                        .style("opacity", 0)
                        .remove();
                    });                

    }

    _vertexUpdate = () => {
        const {structuralVertices, areaSelector, chunk} = this.props;

        /*let currentVertices = chunk.vertices.filter((d) => 
            d.frames.cumulative[currentFrame] > 0); //CHANGE BETWEEN CUMULATIVE AND INSTA*/

        let circles = d3.select(`${areaSelector} svg g.nodes`).selectAll("circle")
            .data(chunk.vertices.filter((d) => d.vertexSector === undefined || d.vertexSector === null), (d) => "node-" + d.id);            
    
        this._updateDrawingGrid();

        circles.exit()
            .transition(450)
            .style("opacity", 0)
            .remove();

        circles
            .transition(450)
            .attr("cx", (d) => this._coordsTranslator(this._getCoordinate(d.id).x, 0))
            .attr("cy", (d) => this._coordsTranslator(this._getCoordinate(d.id).y, 1))
            .attr("r", (d) => this._vertexGirth(d))
            .style("fill", (d) => this._vertexColor(d))
            .style("opacity", (d) => this._vertexOpacity(d));                       

        circles
            .enter()
            .append("circle")
            .attr("class", (d) => {
                let v = structuralVertices.get(d.id);  
                if(v.vertexSector !== undefined && v.vertexSector !== null)
                    return `node-${d.id} vsector${v.vertexSector.x}-${v.vertexSector.y}`;
                return "offScreen";
            })
            .attr("cx", (d) => this._coordsTranslator(this._getCoordinate(d.id).x, 0))
            .attr("cy", (d) => this._coordsTranslator(this._getCoordinate(d.id).y, 1))
            .attr("r", (d) => this._vertexGirth(d))
            .style("fill", (d) => this._vertexColor(d))    
            .style("opacity", 0)
            .on('mouseenter', (d, event) => this._vertexMouseEnter(event))
            .on('mouseleave', (d) => this._vertexMouseOut())
            .transition(450)
            .style("opacity", (d) => this._vertexOpacity(d));   

    }

    _verticesAndEdgesReposition = () => {
        const{areaSelector} = this.props;
        //const{verticesMap} = chunk;

        let circles = d3.select(`${areaSelector} svg`)
        .selectAll("circle");
        
        if(circles.size() >0)
            circles.transition(600)
            .ease(d3.easeCubic)
            .attr("cx", (d) => this._coordsTranslator(this._getCoordinate(d.id).x, 0))
            .attr("cy", (d) => this._coordsTranslator(this._getCoordinate(d.id).y, 1));        

        let lines = d3.select(`${areaSelector} svg`)
            .selectAll("line.active, line.structural");      

        if(lines.size() > 0){
            lines.transition(600)
            .ease(d3.easeCubic)
            .attr("x1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).x, 0))
            .attr("y1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).y, 1))
            .attr("x2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).x, 0))
            .attr("y2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).y, 1))
        }
    }

    _vertexGirth = (vertex) => {
        const {currentFrame} = this.props;

        if(vertex.frames.cumulative === null || vertex.frames.cumulative === undefined || vertex.frames.cumulative[currentFrame] === 0)
            return this.INACTIVE_VERTEX_GIRTH;
        else{
            if(vertex.frames.cumulative[0] === 1.0)
                return this.SEED_GIRTH;
            else if(vertex.frames.cumulative[currentFrame] > 0)
                    return this.ACTIVE_VERTEX_GIRTH;
                else if(currentFrame > vertex.frames.cumulative.length - 1)
                    return this.ACTIVE_VERTEX_GIRTH;
        }
        //if(vertex.frames.cumulative[currentFrame] > 0)
            //return this.ACTIVE_VERTEX_GIRTH;
        
        return this.INACTIVE_VERTEX_GIRTH;        
    }

    _vertexColor = (vertex) => {
        const {currentFrame} = this.props;
        const {vertexBrightnessScale} = this.state; 
        if(vertex.frames.cumulative === null || vertex.frames.cumulative === undefined || vertex.frames.cumulative[currentFrame] === 0)
            return this.INACTIVE_VERTEX_COLOR;
        else{
            if(/*currentFrame === 0 &&*/ vertex.frames.cumulative[0] === 1.0)
                return this.SEED_COLOR;
            if(vertex.frames.cumulative[currentFrame] > 0)
                return vertexBrightnessScale(vertex.frames.cumulative[currentFrame]);
            else if(currentFrame > vertex.frames.cumulative.length - 1)
                    return vertexBrightnessScale(vertex.frames.cumulative[vertex.frames.cumulative.length - 1]);
                else
                    return this.INACTIVE_VERTEX_COLOR;
                //this.ACTIVE_VERTEX_COLOR;
        }
        //return this.INACTIVE_VERTEX_COLOR;
    }

    _vertexOpacity = (vertex) => {
        const {currentFrame} = this.props;
        
        return (vertex.frames.cumulative === null || vertex.frames.cumulative[currentFrame] === 0.0) ? this.INACTIVE_VERTEX_OPACITY : this.ACTIVE_VERTEX_OPACITY;
    }

    _updateStructureLinks = () => {
        const {structuralEdgeOpacity} = this.state;
        const {areaSelector, structuralEdges} = this.props;

        let links = d3.select(`${areaSelector}`)
            .select("svg g.structure").selectAll("line.structural")
            .data(structuralEdges, (d) => d.source + "-" + d.target);
        
        links
            .enter()    
            .append("line")
            .attr("class","structural")
            //.attr('marker-end','url(#structure-arrowhead)')
            .style("stroke", this.STRUCTRUAL_EDGE_COLOR)
            .style("stroke-width", this.STRUCTURAL_STROKE_WIDTH)
            .style("opacity", 0)
            .attr("x1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).x, 0))
            .attr("y1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).y, 1))
            .attr("x2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).x, 0))
            .attr("y2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).y, 1))
            .transition(400)
            .style("opacity", (d) => structuralEdgeOpacity(d.weight))
            ;//this.STRUCTURAL_EDGE_OPACITY);

        links
            .transition(400)
            .attr("x1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).x, 0))
            .attr("y1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).y, 1))
            .attr("x2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).x, 0))
            .attr("y2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).y, 1));       

        links.exit()
            .transition(400)
            .style("opacity", 0)
            .remove();    
    }

    _disableStructureLinks = () => {
        const {areaSelector} = this.props;

        d3.select(`${areaSelector}`).select("svg").selectAll("line.structural")
            .transition(400)
            .attr("opacity", 0)
            .remove();    
    }

    _getInfectionValue = (d) => {
        const{currentFrame} = this.props;
        if(d.frames.cumulative[currentFrame] !== undefined)            
            return d.frames.cumulative[currentFrame];
        else if(currentFrame > d.frames.cumulative.length - 1)
            return d.frames.cumulative[d.frames.cumulative.length - 1];    
    }

    _updateActiveEdges = (currentFrame) => {
        const {areaSelector, chunk} = this.props;
        const {edgeInfectionScale} = this.state;
        //const {verticesMap} = chunk;   
        
        //let diff = currentFrame - activeSetFrame;
        //let remove = diff < 0;
        let lines = d3.select(`${areaSelector}`)
            .select("svg g.edges").selectAll("line.active");      

        /*let links = 
            lines.data(
                newData === undefined ? [] : newData, (d) => this._getEdgeId(d));*/

        let currentEdges = chunk.edges; //CHANGE BETWEEN CUMULATIVE AND INSTA

        if(currentEdges === undefined || currentEdges === null)
            currentEdges = [];

                //let currentEdges = chunk.edges.filter((d) => 
        //    d.frames.cumulative[currentFrame] > 0); //CHANGE BETWEEN CUMULATIVE AND instant

        let links = lines
            .data(currentEdges, (d) => this._getEdgeId(d));           
        
        links.enter()    
            .append("line")
            .attr("class","active")
            //.attr('marker-end','url(#active-arrowhead)')
            .style("stroke", (d) => edgeInfectionScale(this._getInfectionValue(d)))//this.ACTIVATED_EDGE_COLOR)
            .style("stroke-width", this.ACTIVATED_STROKE_WIDTH)
            .style("opacity", 0)
            .attr("x1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).x, 0))
            .attr("y1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).y, 1))
            .attr("x2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).x, 0))
            .attr("y2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).y, 1))
            .transition(400)
            .style("opacity", (d) => this._getInfectionValue(d));//this.ACTIVATED_EDGE_OPACITY);

        links
            .transition(400)
            .style("stroke", (d) => edgeInfectionScale(this._getInfectionValue(d)))//this.ACTIVATED_EDGE_COLOR)
            .style("stroke-width", this.ACTIVATED_STROKE_WIDTH)
            .attr("x1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).x, 0))
            .attr("y1", (d) => 
                this._coordsTranslator(this._getCoordinate(d.source).y, 1))
            .attr("x2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).x, 0))
            .attr("y2", (d) => 
                this._coordsTranslator(this._getCoordinate(d.target).y, 1))   
            .style("opacity", (d) => this._getInfectionValue(d));//this.ACTIVATED_EDGE_OPACITY);

        //if(remove || force)
            links
                .exit()
                .transition(400)
                .style("opacity", 0)
                .remove();

    }

    _toggleActiveEdges = () => {
        const {areaSelector} = this.props;

        d3.select(`${areaSelector}`)
        .select("svg g.edges").selectAll("line.active")
        .transition(400)
        .style("opacity",0)
        .remove(); 
    }

    _getEdgeId = (edge) => {
        return "(" + edge.source + "-" + edge.target + ")";
    }

    _vertexMouseEnter = (hVertex) => {
        const {structuralVertices, onVertexHover} = this.props;
        const {stateFrame} = this.state;
        
        let svertex = structuralVertices.get(hVertex.id);
        let points = d3.selectAll("circle.node-" + svertex.id);
        points.each((vertex, ind) => {
            let node = d3.select(points.nodes()[ind]).node().getBoundingClientRect();
            let nodeString = `Node id: ${vertex.id} ${svertex.vertexSector.x}-${svertex.vertexSector.y}`;
            let simString = "Does not get activated";
            if(vertex.frames.cumulative !== null){
                if(vertex.frames.cumulative[0] > 0){
                    simString = "Seed of simulation";
                }else if(vertex.frames.cumulative[stateFrame] === 0){
                        let steps = 0;
                        for(let t = stateFrame + 1; t< vertex.frames.cumulative.length; t++){
                            steps++;
                            if(vertex.frames.cumulative[t] > 0)
                                break;
                        }
                        simString = "Active in " + steps + " frames";
                    }else{
                        if(vertex.frames.cumulative.length < stateFrame + 1){
                            let cumulativeMaxValue = vertex.frames.cumulative[vertex.frames.cumulative.length - 1];
                            simString = "Activation Prob.: " + cumulativeMaxValue;
                        }
                        else
                            simString = "Activation Prob.: " + vertex.frames.cumulative[stateFrame];
                    }
            }            
     
            d3.select("body").append("div")
            .attr("class", "tooltips")
            .style("display", "none")
            .style("position", "absolute")
            .style('top', (node.top + node.height) + "px")
            .style('left', (node.left + node.width) + "px")
            .html(
                `${nodeString}<br/>
                ${simString}`
            )                    
            .transition().duration(300)
            .style("display", "block"); 
        });        

        points
        .classed("highlighted", true)
        .transition(200)
        .attr("r", this.HIGHLIGHTED_VERTEX_GIRTH)
        .style("fill", this.HIGHLIGHTED_VERTEX_COLOR)
        .attr("opacity", this.HIGHLIGHTED_VERTEX_OPACITY);

        onVertexHover(hVertex);
    }

    _vertexMouseOut = (vertex) => {  
        const {onVertexHover} = this.props;

        d3.selectAll('div.tooltips')
            .transition().duration(200)
            .style("opacity", 0)
            .remove();
        
        d3.selectAll("circle.highlighted")  
        .classed("highlighted", false)      
        .transition(200)
        .attr("r", (d) => this._vertexGirth(d))
        .style("fill", (d) => this._vertexColor(d))    
        .style("opacity", (d) => this._vertexOpacity(d));

        onVertexHover();
   }

    _isVertexActive(vertex){
        const {currentFrame} = this.props;
        if(vertex.active_at < 0)
            return false;
        else if(vertex.active_at <= currentFrame)
            return true;
        return false;
    }

    /*_isVertexSeed = (vertex) => {
        return vertex.active_at === 0;
    }*/

    _getCoordinate = (vertex) => {
        const {structuralVertices} = this.props;
        return structuralVertices.get(vertex).global_coords;
        //const {storedLocalCoordinates} = this.state;
        //return storedLocalCoordinates ?
        //        vertex.coords : structuralVertices.get(vertex).global_coords;
    }

    _coordsTranslator = (coords, dim) => {
        const {clickedSectors} = this.props;
        const {xRatio, yRatio, cellWidth} = this.state;

        //return dim == 0 ? (coords-bBox.minX)*xRatio + PADDING_X : (coords-bBox.minY)*yRatio + PADDING_Y;
        
        //let finCoords = dim === 0 ? (coords-bBox.minX)*xRatio : (coords-bBox.minY)*yRatio;
        //let finCoords = dim === 0 ? (coords-Number.parseInt(clickedSectors.start_x*cellWidth))*xRatio : (coords-Number.parseInt(clickedSectors.start_y*cellWidth))*yRatio;
        let finCoords = dim === 0 ? (coords-clickedSectors.start_x*cellWidth)*xRatio : (coords-clickedSectors.start_y*cellWidth)*yRatio;

        /*let finCoords = {x: (coords.x-minX)*xRatio, y:(coords.y-minY)*yRatio};
        if(finCoords.x >= localBounds.width/2){
            finCoords.x -= PADDING_X;
        }else
            finCoords.x += PADDING_X;
        if(finCoords.y >= localBounds.height/2)
            finCoords.y -= PADDING_Y;
        else
            finCoords.y += PADDING_Y;*/
        return finCoords;
    }

    /* #### CODE TO ENABLE ARROWS
                        <defs>
                        <marker id="structure-arrowhead" viewBox="-0 -5 10 10" refX="13" refY="0" orient="auto" markerWidth="7" markerHeight="7" xoverflow="visible">
                            <path d="M 0,-5 L 10 ,0 L 0,5" fill="black" style={{stroke: "black"}}></path>
                        </marker>
                        <marker id="active-arrowhead" viewBox="-0 -5 10 10" refX="13" refY="0" orient="auto" markerWidth="5" markerHeight="5" xoverflow="visible">
                            <path d="M 0,-5 L 10 ,0 L 0,5" fill="transparent" style={{stroke: "black"}}></path>
                        </marker>
                    </defs>
    */

    render(){
        const {storedHeight} = this.state;
        const {classes, simId} = this.props;
        return(<div>
                <svg className="graphArea" style={{float: "left", border: "solid 4px #2ca25f"}}>
                    <g className="grid"></g>
                    <g className="structure"></g>
                    <g className="edges"></g>
                    <g className="nodes"></g>
                </svg>
                <div className={classes.legendContainer}>
                    <div id={`sim${simId}-vGLegend`} style={{display: "block", width: "100%"}}>
                        <ContinuosLegendComponent 
                            customText={"Vertex Activation Prob."}
                            colorScheme={d3.interpolatePurples}
                            areaSelector={`#sim${simId}-vGLegend`}
                            orientation={VERTICAL_ORIENTATION}
                            domain={[0, 1]}
                            corner={SNIP_RIGHT}
                            height={storedHeight === undefined ? undefined : parseInt(Math.ceil(storedHeight/2) - 5)}
                        />    
                    </div>
                    <div id={`sim${simId}-eGLegend`} style={{display: "block", width: "100%"}}>
                        <ContinuosLegendComponent 
                            customText={"Link Activation Prob."}
                            colorScheme={d3.interpolateReds}
                            areaSelector={`#sim${simId}-eGLegend`}
                            orientation={VERTICAL_ORIENTATION}
                            domain={[0, 1]}
                            corner={SNIP_RIGHT}
                            height={storedHeight === undefined ? undefined : parseInt(Math.ceil(storedHeight/2) - 5)}
                        />    
                    </div>
                </div>
        </div>);
    }
    
}


const GraphComponent = withStyles(GRAPH_STYLES)(GraphWindow);

export default GraphComponent;