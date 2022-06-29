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
//import {getSuffixString} from "../utils/utils";

const transformX = 40;
const transformY = 30;

/*
 * PROPS:
 * graphdata, simulationFrame, considerSeeds, showDensity, showInfection
 * */

const AREA_CHART_STYLES = {
    root: {
        width: "100%"
    }
}

class AreaChart extends React.Component {

    constructor(props){
        super(props);
        this.state = {xAxis: {}, yAxis: {}, zeroArray: {}};
    }

    componentDidUpdate(){
        const{areaSelector, simulation, currentFrame, hoveredVertex} = this.props;
        const {aggregation} = simulation; 
        const{xAxis, yAxis, zeroArray} = this.state;
        
        //MOVE NEEDLE
        d3.select(`${areaSelector}`).select('svg').select("line.frameNeedle")
            .transition().duration(150)
            .attr("x1", xAxis(currentFrame))
            .attr("x2", xAxis(currentFrame));

        let svg = d3.select(`${areaSelector}`)
            .select('svg')

        let frames = [];
        let cumulativeFrames = [];
        /*let finZeroArray;
        if(prevProps.maxSimsFrames !== maxSimsFrames){
            let finZeroArray = [];
            for(let t=0; t<maxSimsFrames; t++)
                finZeroArray[t] = 0;   
            this.setState({...this.state, xAxis: xAxis, zeroArray: finZeroArray}, () => {
                d3.select(`${areaSelector}`).select("g.bottomLegend")
                .call(d3.axisBottom(xAxis).ticks(maxSimsFrames));
            });
            return;
        }
        //else
         //   finZeroArray = zeroArray;
        */

        if(hoveredVertex !== undefined && hoveredVertex.frames.cumulative !== null && hoveredVertex.frames.instant !== null){
            frames = hoveredVertex.frames.instant;
            cumulativeFrames = hoveredVertex.frames.cumulative;
        }else{
            frames = zeroArray;
            cumulativeFrames = zeroArray;
        }


        // Cumulative Infected per day chart
        svg.select("path.cumulative")
        .datum(cumulativeFrames)
        .transition(400)
        .attr("d", d3.area()
            .x((d,i) => {return xAxis(i*aggregation) })
            .y0(yAxis(0))
            .y1(function(d) { return yAxis(d) })
        );

        // Infected per day chart
        svg.select("path.istantaneous")
            .datum(frames)
            .transition(400)
            .attr("d", d3.area()
                .x((d,i) => {return xAxis(i*aggregation) })
                .y0(yAxis(0))
                .y1(function(d) { return yAxis(d) })
            );            

        //if(zeroArray.length !== finZeroArray.length)
        //   this.setState({...this.state, zeroArray: zeroArray});
    }

    componentDidMount(){        
        this._generateDomElement();
    }

    render(){
        return (<svg></svg>);
    }

    _generateDomElement = () => {
        const {areaSelector, simulation,/* maxSimsFrames,*/ currentFrame} = this.props;
        const {aggregation, end_frame} = simulation; 
        //d3.select(`${areaSelector}`).selectAll('svg').remove();
        let node = d3.select(`${areaSelector}`).node();
        let width = node === null ? 490 : node.getBoundingClientRect().width;
        let height = node === null ? 490 : node.getBoundingClientRect().height;

        //height = height > width ? width : height;

        let zeroArray = [];

        for(let t=0; t<end_frame; t++)
            zeroArray[t] = 0;

        let svg = d3.select(`${areaSelector}`)
        .select('svg');

        svg.attr('width', width )
        .attr('height', height );
       
        // Add X axis --> it is a date format
        var x = d3.scaleLinear();

            x.domain([0, end_frame*aggregation])
            .range([ 0, width - transformX - 10 ]);
            
            // let ticks = x.ticks(maxSimsFrames);
            // let tickFormat = x.tickFormat(maxSimsFrames, "d");

            // ticks.map(tickFormat);

            // x.tickValues(ticks);

        svg.append("g")
        .classed("bottomLegend", true)
        .attr("transform", "translate(" + transformX +"," + (height - transformY) + ")")
        .call(d3.axisBottom(x).ticks(end_frame));

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0,1])
            .range([ height, transformY + 5 ]);
            //.domain([0, maxActive])//d3.max(frames, function(d) { return +d.activated; })]            
        //y;
        
        //y.ticks();
            //.map((d) => getSuffixString(d));

        svg.append("g")
            .attr("transform", "translate(" +transformX+" , -" + transformY + ")")
            .call(d3.axisLeft(y).tickFormat(d3.format(".1f")));
        
        svg.append("g")
        .attr("transform", "translate(" +transformX+" , 0)")
        .append("line")
        .attr("class", "frameNeedle")
        .attr("x1",x(currentFrame))
        .attr("y1", 5)
        .attr("x2",x(currentFrame))
        .attr("y2", height - transformY)
        .style("stroke", "red")
        .style("stroke-width", "3px");

        let gsvg = svg.append("g");

        gsvg.append("path")
        .datum(zeroArray)
        .attr("class", "cumulative")
        .attr("fill", "lightgreen")
        .attr("stroke", "black")
        .attr("stroke-width", 1.5)
        .attr("opacity", "0.45")
        .attr("transform", "translate(" +transformX+" , -" + transformY + ")")
        .attr("d", d3.area()
            .x((d,i) => {return x(i*aggregation) })
            .y0(x(0))
            .y1(function(d) { return y(d) })
        );
        

        gsvg.append("path")
        .datum(zeroArray)
        .attr("class", "istantaneous")
        .attr("fill", "#cce5df")
        .attr("stroke", "#69b3a2")
        .attr("stroke-width", 1.5)
        .attr("opacity", "0.7")
        .attr("transform", "translate(" +transformX+" , -" + transformY + ")")
        .attr("d", d3.area()
            .x((d,i) => {return x(i*aggregation) })
            .y0(y(0))
            .y1(function(d) { return y(d) })        
        );

        // text label for the y axis
        svg.append("text")
        .attr("dy", "1em")
        .attr("transform", "rotate(-90)")
        .attr("y", -2)
        .attr("x",  -((height - transformY + 5)/2))
        .attr("font-size", "smaller")
        .attr("font-weight", "bold")
        .style("text-anchor", "middle")
        .text("Act. Prob.");     
        
        // text label for the x axis
        let xText = svg.append("text")
        .attr("font-size", "smaller")
        .attr("font-weight", "bold")                       
        .style("text-anchor", "middle")
        .text("Time");     

        xText.attr("transform",
        "translate(" + (width - xText.node().getBoundingClientRect().width - 5) + " ," + 
                       (height - 17) + ")")

        this.setState({...this.state, xAxis: x, yAxis: y, zeroArray: zeroArray});
    }

}

const AreaChartComponent = withStyles(AREA_CHART_STYLES)(AreaChart);

export default AreaChartComponent;