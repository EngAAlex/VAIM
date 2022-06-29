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

import React from 'react';
import * as d3 from 'd3';
import {withStyles} from "@material-ui/core/styles";

const transformX = 40;
const transformY = 30;

const AREA_CHART_STYLES = {
    root: {
        width: "100%"
    }
}

class BoxWhisker extends React.Component {

    constructor(props){
        super(props);
        this.state = {xAxis: {}};
    }

    componentDidUpdate(){
        const{areaSelector, currentFrame} = this.props;
        const{xAxis} = this.state;
        d3.select(`${areaSelector}`).select('svg').select("line.frameNeedle")
            .transition().duration(150)
            .attr("x1", xAxis(currentFrame))
            .attr("x2", xAxis(currentFrame));
    }

    componentDidMount(){        
        this._generateDomElement();
    }

    render(){
        return (<svg></svg>);
    }

    _generateDomElement = () => {
        const {areaSelector, simulation, maxActive,/* maxSimsFrames,*/ currentFrame} = this.props;
        const {aggregation, frames, end_frame} = simulation; 
        //d3.select(`${areaSelector}`).selectAll('svg').remove();
        let node = d3.select(`${areaSelector}`).node();
        let width = node === null ? 490 : node.getBoundingClientRect().width;
        let height = node === null ? 490 : node.getBoundingClientRect().height;

        //height = height > width ? width : height;

        let svg = d3.select(`${areaSelector}`)
        .select('svg')
        .attr('width', width )
        .attr('height', height );

        var x = d3.scaleLinear();

        x.domain([0, end_frame*aggregation])
        .range([ 0, width - transformX - ((width-transformX)/end_frame/2)/* - xPadding */]);
            
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
            .domain([0, maxActive + 0.5]).nice()//d3.max(frames, function(d) { return +d.activated; })])
            .range([ height, transformY + 5]);
        //y;
        
        //y.ticks();
            //.map((d) => getSuffixString(d));

        svg.append("g")
            .attr("transform", "translate(" +transformX+" , -" + transformY + ")")
            .call(d3.axisLeft(y).tickFormat(d3.format(".2s")));

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

        // text label for the y axis
        svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", -2)
        .attr("x",  -((height - transformY + 5)/2))
        .attr("dy", "1em")
        .attr("font-size", "smaller")
        .attr("font-weight", "bold")
        .style("text-anchor", "middle")
        .text("Activations");     

        // text label for the x axis
        let xText = svg.append("text")
        .attr("font-size", "smaller")
        .attr("font-weight", "bold")                       
        .style("text-anchor", "middle")
        .text("Time");     

        xText.attr("transform",
        "translate(" + (width - xText.node().getBoundingClientRect().width - 5) + " ," + 
                       (height - 17) + ")")

        var bins = 
        /*d3.bin()
            .thresholds(maxSimsFrames)
            .value(d => d.frame)
        (frames)*/
            frames.slice(1, frames.length).map((bin, index) => {
            //bin.sort((a, b) => a.min - b.min);
            //const values = bin.map(d => d.min);
            /*const min = values[0];
            const max = values[values.length - 1];
            const q1 = d3.quantile(values, 0.25);
            const q2 = d3.quantile(values, 0.50);
            const q3 = d3.quantile(values, 0.75);*/
            const q1 = bin.low_quartile;
            const q2 = bin.median;
            const q3 = bin.high_quartile;
            if(q1 === undefined || q2 === undefined || q3 === undefined){
                bin.undefined = true;
                return bin;
            }
            /*const iqr = q3 - q1; // interquartile range
            const r0 = Math.max(bin.min_activated, q1 - iqr * 1.5);
            const r1 = Math.min(bin.max_activated, q3 + iqr * 1.5);*/
            bin.quartiles = [q1, q2, q3];
            //bin.range = [r0, r1];
            bin.range = [bin.min_activated, bin.max_activated];
            //bin.outliers = bin.filter(v => v.y < r0 || v.y > r1); // TODO
            bin.frame = index + 1; //0 frame is removed            
            return bin;
            });



        const g = svg.append("g")
            .attr("transform", `translate(${transformX},-${transformY})`)
            .selectAll("g")
            .data(bins)//binner(bins).filter(d => d[0] !== undefined && !d[0].undefined)) //binner(bins)
            .join("g");        
        
        g.append("rect")
            .style("fill", "rgba(63, 81, 181, 0.2)")
            .style("stroke", "rgba(63, 81, 181, 0.7)")
            .style("stroke-width", "0.5")    
            .attr("x", (d) => x(d.frame - 0.5))
            .attr("y", (d) => y(d.quartiles[2]))
            .attr("width", (d) => x(1))
            .attr("height", (d) => y(d.quartiles[0]) - y(d.quartiles[2]));                 

        g.append("line")
           .attr("x1", (d) => x(d.frame))
           .attr("y1", (d) => y(d.range[1]))
           .attr("x2", (d) => x(d.frame))
           .attr("y2", (d) => y(d.range[0]))
           .style("stroke", "black")
           .style("stroke-width", "3px");         

        g.append("line")
           .attr("x1", (d) => x(d.frame - 0.5))
           .attr("y1", (d) => y(d.quartiles[1]))
           .attr("x2", (d) => x(d.frame + 0.5))
           .attr("y2", (d) => y(d.quartiles[1]))
           .style("stroke", "black")
           .style("stroke-width", "1px"); 

        /*
        #######OLD CODE
        const binner = d3.bin()
        .domain([0, end_frame + 1])
        .value(d => d.frame);

        /*g.append("path")
            .attr("stroke", "currentColor")
            .attr("d", d => `
                M${x(d.frame + 0.5)},${y(d.range[1])}
                V${y(d.range[0])}
            `);
            

        /*g.append("path")
            .attr("fill", "#ddd")
            .attr("d", d => `
                M${x(d.frame)},${y(d.quartiles[0])}
                H${x(d.frame + 1)}
                V${y(d.quartiles[2])}
                H${x(d.frame)}
                Z
            `);
        

            
        /*g.append("path")
            .attr("stroke", "currentColor")
            .attr("stroke-width", 2)
            .attr("d", d => `
                M${x(d.frame)},${y(d.quartiles[1])}
                H${x(d.frame + 1)}
            `);
                  

        /*g.append("g")
            .attr("fill", "currentColor")
            .attr("fill-opacity", 0.2)
            .attr("stroke", "none")
            .attr("transform", d => `translate(${x((d.x0 + d.x1) / 2)},0)`)
            .selectAll("circle")
            .data(d => d.outliers)
            .join("circle")
            .attr("r", 2)
            .attr("cx", () => (Math.random() - 0.5) * 4)
            .attr("cy", d => y(d.y));*/

        this.setState({...this.state, xAxis: x});
    }

}

const BoxWhiskerComponent = withStyles(AREA_CHART_STYLES)(BoxWhisker);

export default BoxWhiskerComponent;