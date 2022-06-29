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
import {withStyles} from "@material-ui/core/styles";
import ContinuosLegendComponent, {VERTICAL_ORIENTATION, HORIZONTAL_ORIENTATION, LEGEND_AREA_MINOR_AXIS_EXTENT, SNIP_TOP, SNIP_LEFT} from '../panels/continousLegend.js'

//const legendTransformX = 8;
const transformX = 0;
const transformY = 0;

//export const legendAreaHeight = 45;         
//export const efficiencyBuckets = 3 ;
/*
 * PROPS:
 * graphdata, simulationFrame, considerSeeds, showDensity, showInfection
 * */

const MATRIX_STYLE = {
    root: {
        width: "100%"
    },
    topLegend: {
        display: "inline-block",
        height: `${LEGEND_AREA_MINOR_AXIS_EXTENT}px`,
        width: "100%"
    },
    leftLegend: {
        position: "relative",
        float: "left",
        width: `${LEGEND_AREA_MINOR_AXIS_EXTENT}px`,
        height: "100%"
    }
}

class Matrix extends React.Component {

    constructor(props){
        super(props);
        this.state = {colorScheme: props.colorScheme, showInfection: props.showInfection, brushingActive: false, storedPeekSeeds: props.peekSeeds,
                    storedShowLow: false, storedShowHigh: false};
    }

    componentDidUpdate(prevProps){
        const {/*x, y, seedsDist,*/ matrixData, clickedSectors, cellValue, activateBrushing, onCellBrushed,
            areaSelector, resolution, currentFrame, peekSeeds, showHighInfection, showLowInfection, fullDist} = this.props;
        const {storedResolution, storedFrame, storedPeekSeeds, brush, colorScheme, storedShowLow, storedShowHigh, 
            showInfection, brushingActive, maxCounter, cellWidth, fullEfficiencies} = this.state;

        //let activeFrame = drawnFrame === undefined ? {nodes: givenFrame.nodes, edges: infectionsArray[currentFrame]} : drawnFrame;
        
        if(!showInfection && brushingActive !== activateBrushing){
            this.setState({...this.state, brushingActive: activateBrushing}, () => this._generateDomElement());
            return;
        }

        if(storedResolution !== resolution/* || (infectionsArray !== undefined && infectionsArray[0].length !== resolution)*/){
            var oldResolution = storedResolution;        
            let maxCounter = Number.MIN_SAFE_INTEGER;
            let fullEfficiencies = new Map();            
            if(showInfection){
                maxCounter = 1;
                for(let curr of fullDist){
                    fullEfficiencies.set(curr.sectorId, cellValue(curr));                    
                }
            }
            else
                for(let curr of matrixData){                    
                    maxCounter = Math.max(maxCounter, cellValue(curr));
                }
            this.setState({...this.state, maxCounter: maxCounter, storedResolution: resolution, fullEfficiencies: fullEfficiencies}, () => {
                this._generateDomElement(() => {
                    if(brushingActive && clickedSectors !== null){
                        const {cellWidth} = this.state;
                        const {start_x, start_y, span_x, span_y} = clickedSectors;
                        //let cellWidth = (d3.select(`${areaSelector} svg.matrixArea`).node().getBoundingClientRect().width - 5)/resolution;
                        let computedRects = this._computeRectangles([[start_x*cellWidth, start_y*cellWidth], [start_x*cellWidth + (span_x*oldResolution/resolution)*cellWidth, start_y*cellWidth + span_y*oldResolution/resolution*cellWidth]], cellWidth);                                                     
                        onCellBrushed(computedRects);                            
                        d3.select(`${areaSelector} svg.matrixArea g.base`).transition().call(brush.move, computedRects.snappedCoords);                            
                    }
                });                
            });
            return;
        }

        if(showInfection){
            let changeDetected = false;
            let changeObject = {};
            var thisSvg = d3.select(`${areaSelector} svg.matrixArea g.base`);

            if(storedFrame !== currentFrame){

                changeDetected = true;
                changeObject = {...changeObject, storedFrame: currentFrame};
            
                var colorScale = d3.scaleLinear().domain([0, maxCounter]).range([0,1]);
                thisSvg.selectAll('rect.matrixRect')
                    .data(matrixData, (d) => d.sectorId)//.enter().select('rect')
                    .transition(500).style('fill', d => {
                        if(showInfection)
                            if(cellValue(d) <= 0)
                                return "white";
                        return colorScheme(colorScale(cellValue(d)));
                    });
            }

            let classToActive = [];
            let classNotActive = [];
            
            if(showLowInfection !== storedShowLow){
                if(showLowInfection){
                    thisSvg.selectAll('rect.matrixRect.lowInfection').each(
                    (d) => {
                            let textSize = 15;
                            let fontWeight = "bold";

                            let text = thisSvg.append("text")
                                .classed("tempRateLow", true)
                                .attr("text-size", textSize+"px")
                                .attr("font-weight", fontWeight)
                                .text(parseInt(fullEfficiencies.get(d.sectorId)*100));

                            text
                                .attr("x",  d.sectorId.split("-")[0] * cellWidth + cellWidth/2 - text.node().getComputedTextLength()/2 )
                                .attr("y", d.sectorId.split("-")[1] * cellWidth + cellWidth/2 + textSize*0.5);
                                
                    });
                    classToActive.push("lowInfection");
                }else{
                    thisSvg.selectAll("text.tempRateLow").remove();
                    classNotActive.push("lowInfection");
                }
                changeDetected = true;
                changeObject = {...changeObject, storedShowLow: showLowInfection};
            }

            if(showHighInfection !== storedShowHigh){
                if(showHighInfection){
                    thisSvg.selectAll('rect.matrixRect.highInfection').each(
                        (d) => {
                                let textSize = 15;
                                let fontWeight = "bold";
        
                                let text = thisSvg.append("text")
                                    .classed("tempRateHigh", true)
                                    .attr("text-size", textSize+"px")
                                    .attr("font-weight", fontWeight)
                                    .text(parseInt(fullEfficiencies.get(d.sectorId)*100));
        
                                text
                                    .attr("x",  d.sectorId.split("-")[0] * cellWidth + cellWidth/2 - text.node().getComputedTextLength()/2 )
                                    .attr("y", d.sectorId.split("-")[1] * cellWidth + cellWidth/2 + textSize*0.5);
                                    
                        });

                    classToActive.push("highInfection");
                }else{
                    thisSvg.selectAll("text.tempRateHigh").remove();
                    classNotActive.push("highInfection");            
                }            
                changeDetected = true;
                changeObject = {...changeObject, storedShowHigh: showHighInfection};
            }

            let classes = "";
            if(classToActive.length > 0){
                for(let s in classToActive){
                    classes += "rect."+classToActive[s];
                    if(parseInt(s) !== classToActive.length - 1)
                        classes += ",";
                }
                
                thisSvg.selectAll(classes)
                    .classed("infectionHighlighted", true);

            }

            classes = "";
            if(classNotActive.length > 0){
                for(let s in classNotActive){
                    classes += "rect."+classNotActive[s];
                    if(parseInt(s) !== classNotActive.length - 1)
                        classes += ",";
                }
                
                thisSvg.selectAll(classes)
                .classed("infectionHighlighted", false);
            }
            

            if(storedPeekSeeds !== peekSeeds){
                changeDetected = true;
                changeObject = {...changeObject, storedPeekSeeds: peekSeeds};
                if(peekSeeds)
                    thisSvg
                        .selectAll('rect.seeder')
                        .classed("peeking", true)
                else
                    thisSvg
                        .selectAll('rect.peeking')
                        .classed("peeking", false)
            }

            if(prevProps.clickedSectors !== clickedSectors && 
                clickedSectors.snappedCoords !== undefined && 
                clickedSectors.snappedCoords.length === 2){
                const {start_x, start_y, span_x, span_y} = clickedSectors;
                let arr = [];
                arr.push({coords: [start_x, start_y], span: [span_x, span_y], id: "selection"});
                //arr.push({coords: [span_x + start_x, span_y+start_y], id: "end"});
                
                //let size = 500;

                let overlay = d3.select(`${areaSelector} svg.matrixArea g.overlay`);

                let square = 
                    overlay.selectAll("rect")
                    .data(arr, (d) => d.id);

                square.enter()
                    .append("rect")
                    .attr("class", "markers")
                    .attr("x", (d) => d.coords[0]*cellWidth)
                    .attr("y", (d) => d.coords[1]*cellWidth)
                    .attr("width", (d) => d.span[0]*cellWidth)
                    .attr("height", (d) => d.span[1]*cellWidth)
                    .style("fill","transparent")
                    .style("stroke", "#2ca25f")
                    .style("stroke-width", "3px")
                    .style("opacity",0)
                    .transition(400)
                    .style("opacity", 1);   
                
                square.transition(400)
                    .attr("x", (d) => d.coords[0]*cellWidth)
                    .attr("y", (d) => d.coords[1]*cellWidth)
                    .attr("width", (d) => d.span[0]*cellWidth)
                    .attr("height", (d) => d.span[1]*cellWidth)

                /*let symbols = thisSvg
                    .selectAll("path.markers")
                    .data(arr, (d) => d.id);

                symbols.enter()
                    .append("path")
                    .attr("class", "markers")
                    .attr("transform", (d) => "translate(" + d.coords[0]*cellWidth + "," + d.coords[1]*cellWidth + ")")
                    .attr("d", d3.symbol().size(size).type(d3.symbolCircle))
                    .style("fill", "cyan")
                    .style("opacity", 0)
                    .transition(400)
                    .style("opacity", 1);       
                    
                symbols
                    .transition(400)
                    .attr("transform", (d) => "translate(" + d.coords[0]*cellWidth + "," + d.coords[1]*cellWidth + ")")*/

            }

            if(changeDetected)
                this.setState({...this.state, ...changeObject});
        }
    }

    componentDidMount(){        
        const {currentFrame, resolution, showInfection, matrixData, fullDist, cellValue} = this.props;
        //let matrixData = this._generateMatrix();
        //let ex_d3 = this._generateDomElement(matrixData, currentd3, overrideSize !== undefined ? overrideSize : 490);

        
        let maxCounter = Number.MIN_SAFE_INTEGER;
        let fullEfficiencies = new Map();            
        if(showInfection){
            maxCounter = 1;
            for(let curr of fullDist){
                fullEfficiencies.set(curr.sectorId, cellValue(curr));                    
            }
        }
        else
            for(let curr of matrixData){                    
                maxCounter = Math.max(maxCounter, cellValue(curr));
            }

        this.setState({...this.state, storedResolution: resolution, storedFrame: currentFrame, maxCounter: maxCounter, fullEfficiencies: fullEfficiencies}, () => {
            //    saveCurrentMatrix(matrixData);
            this._generateDomElement();        
        });
    }

    render(){
        const {classes} = this.props;
        const {colorScheme, showInfection, maxCounter, storedHeight} = this.state;
        const {simId} = this.props;
        let legendIdentifier = simId === undefined ? "vertexDist-legend" : `sim${simId}-legend`;
        return (<div>
                    <div id={legendIdentifier} className={showInfection ? classes.leftLegend : classes.topLegend}>
                        <ContinuosLegendComponent 
                            customText={showInfection ? "Activations" : "Vertex Density"}
                            colorScheme={colorScheme}
                            areaSelector={`#${legendIdentifier}`}
                            orientation={showInfection ? VERTICAL_ORIENTATION : HORIZONTAL_ORIENTATION}
                            domain={[0, maxCounter]}
                            corner={showInfection ? SNIP_LEFT : SNIP_TOP}
                            height={storedHeight}
                        />    
                    </div>
                    <svg style={{border: "solid 1px black"}} className="matrixArea">
                        <g className="base"></g>
                        <g className="overlay" style={{pointerEvents: "none"}}></g>
                    </svg>
                </div>);
    }

    _generateDomElement = (callback) => {
        const {areaSelector, matrixData, cellValue, x, y, onCellBrushed, simId, seedsDist, onCellClicked} = this.props;
        const {colorScheme, showInfection, storedResolution, brushingActive, maxCounter, fullEfficiencies} = this.state;
        
        d3.select(`${areaSelector} svg.matrixArea`).selectAll('rect').remove();
        d3.select(`#sim${simId}-legend`).selectAll().remove();

        let node = d3.select(`${areaSelector}`).node();
        let width = node === null ? 490 : node.getBoundingClientRect().width - (showInfection ? (LEGEND_AREA_MINOR_AXIS_EXTENT + 55) : 0);
        //width += showInfection ? 50 : 0; ## activate to enlarge matrix
        
        var height = width; //node === null ? 490 : node.getBoundingClientRect().height;
        //let brush = null;
        //let width = Math.min(nodeWidth, height);
        //let widthDifference = (nodeWidth-width);                

        // let activeFrame = drawnFrame === undefined ? {nodes: givenFrame.nodes, edges: infectionsArray[currentFrame]} : drawnFrame;

        let cellWidth = width/storedResolution;//activeFrame.nodes.length;

        //var dataToPrint = this._generateCells(activeFrame, givenFrame);

        var colorScale = d3.scaleLinear().domain([0, maxCounter]).range([0,1]);
        //var seedsMap = new Map();

        //d3.select(ReactDOM.findDOMNode(this))
        let svg = d3.select(`${areaSelector} svg.matrixArea`)
        .attr("width", width).attr("height", width)
        .attr('transform', 'translate('+ transformX +','+transformY+')');

        svg.select("g.base")
        .selectAll('rect')
        .data(matrixData, (d) => d.sectorId)
        .enter()
        .append('rect')
        .attr('seeds', (d) => {
            if(showInfection)
                for(let c of seedsDist){
                    if(c.sectorId === d.sectorId && cellValue(c) > 0){
                        return c.edgesAggregator/c.verticesAggregator;
                    }
                }
            return "-1";            
        })
        .attr('class', (d) => {
                    let baseClass = 'matrixRect matrixRect-'+d.sectorId;
                        if(showInfection){
                            if(cellValue(d) > 0){
                                baseClass += " active-rect";
                            for(let c of seedsDist){
                                if(c.sectorId === d.sectorId){
                                    if(c.edgesAggregator> 0){
                                        //d.seeds = d.edgesAggregator;
                                        baseClass += " seeder";
                                        //seedsMap.put(x(c)+'-'+y(c), cellValue(c));
                                    }
                                    break;
                                }
                            }
                        }
                        let eff = fullEfficiencies !== undefined ? fullEfficiencies.get(d.sectorId) : cellValue(d);//cellValue(d);
                        if(eff > 0)
                            if(eff < 0.5)
                                baseClass += " lowInfection";
                //                else if (eff < 0.66)
                //                   baseClass += " medInfection";
                            else if(eff >= 0.5)
                                baseClass += " highInfection";
                    }
                return baseClass;
            })
        .attr('eff', (d) => cellValue(d))
        .attr('cumulative_eff', (d) => fullEfficiencies !== undefined ? fullEfficiencies.get(d.sectorId) : cellValue(d))
        .attr('width', cellWidth)
        .attr('height', cellWidth)
        .attr('x', d => x(d)*cellWidth)
        .attr('y', d => y(d)*cellWidth)
        .style('stroke', (d) => {
            if(showInfection){
                for(let c of seedsDist){
                    if(c.sectorId === d.sectorId){
                        return cellValue(c) < 0.01 ? 'lightgrey' : 'black';
                    }
                }                
            }else
                return cellValue(d) < 0.01 ? 'lightgrey' : 'black'}
            )
        .style('stroke-width', (d) => {
            if(showInfection){
                for(let c of seedsDist){
                    if(c.sectorId === d.sectorId){
                        return cellValue(c) < 0.01 ? '1px' : '2px';
                    }
                }                
            }else
                return cellValue(d) < 0.01 ? '1px' : '2px'}
            )
        .style('stroke-opacity', .4)
        .style('fill', d => {
            if(showInfection)
                if(cellValue(d) <= 0)
                    return "white";
            return colorScheme(colorScale(cellValue(d)));
        }).style('fill-opacity', 1)
        .on('mouseenter', (event, d) => {
            let rects = d3.selectAll(`rect.matrixRect-${d.sectorId}, rect.cell-${d.sectorId}`);
            rects
                 .classed("highlightedCell", true);

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
            })
        .on('click', (event, d) => onCellClicked(d));

        var brush;

        if(!showInfection && brushingActive){
            brush = d3.brush()                 // Add the brush feature using the d3.brush function
            .extent( [ [0,0], [width, width] ] ) // initialise the brush area: start at 0,0 and finishes at width,height: it means I select the whole graph area
            .on("end", (event, d) => {
                if(event.sourceEvent){  
                    let computedRects = this._computeRectangles(event.selection, cellWidth);                                                     
                    onCellBrushed(computedRects);                            
                    d3.select(`${areaSelector} svg.matrixArea g.base`).transition().call(event.target.move, computedRects.snappedCoords);                            
                }                            
                //console.log(startPointCell + " " + endPointCell + " " + selectedRectangles);
            })
            d3.select(`${areaSelector} svg.matrixArea g.base`)
            .call(brush);
        }
        this.setState({...this.state, storedHeight: height, cellWidth: cellWidth, brush: brush}, callback);
    }

    _computeRectangles = (extent, cellWidth) => { 
        const {storedResolution} = this.state;  
        if(extent === null || extent === undefined)
            return [];  

        let startPointCell = [Math.round((extent[0][0])/cellWidth), Math.round((extent[0][1])/cellWidth)];
        let endPointCell = [Math.round((extent[1][0])/cellWidth), Math.round((extent[1][1])/cellWidth)];        
        let span_x = endPointCell[0] - startPointCell[0];
        let span_y = endPointCell[1] - startPointCell[1];
        if(span_x === 0 || span_y === 0){
            span_x = 1;
            span_y = 1;
            endPointCell = [startPointCell[0] + 1, startPointCell[1] + 1];
        }

        if(startPointCell[0] + span_x > storedResolution){
            span_x = storedResolution - startPointCell[0];
            endPointCell[0] = storedResolution;
        }
        if(startPointCell[1] + span_y > storedResolution){
            span_y = storedResolution - startPointCell[1];
            endPointCell[1] = storedResolution;
        }        
        let snappedCoords = [[startPointCell[0]*cellWidth, startPointCell[1]*cellWidth], [(endPointCell[0])*cellWidth, (endPointCell[1])*cellWidth]];
        /*let selectedRectangles = [];
            for(let i = startPointCell[0]; i <= endPointCell[0]; i++)
                for(let j = startPointCell[1]; j <= endPointCell[1]; j++)
                    selectedRectangles[selectedRectangles.length] = [i,j];*/
        return {snappedCoords: snappedCoords, start_x: startPointCell[0], start_y: startPointCell[1], span_x: span_x, span_y: span_y};
    }

}

const MatrixComponent = withStyles(MATRIX_STYLE)(Matrix);

export default MatrixComponent;
