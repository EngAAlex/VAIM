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
import * as d3 from 'd3';

const legendPadding = 8;
const transformX = 0;
const transformY = 0;

export const LEGEND_AREA_MINOR_AXIS_EXTENT = 44;
const LEGEND_SVG_EXTENT = 28;

const LEGEND_BAR_HEIGHT = 11;

export const VERTICAL_ORIENTATION = 1;
export const HORIZONTAL_ORIENTATION = 0;

export const SNIP_TOP = 1;
export const SNIP_BOTTOM = 2;
export const SNIP_RIGHT = 3;
export const SNIP_LEFT = 4;


class ContinuosLegend extends Component {

    constructor(props) {
        super(props);
        this.state = { height: props.height, text: props.customText === undefined ? "Activations" : props.customText, areaSelector: props.areaSelector, colorScheme: props.colorScheme, orientation: props.orientation, domain: props.domain, corner: props.corner };
    }

    componentDidUpdate = (prevprops) => {        
        const {orientation, height} = this.state;
            if(prevprops !== this.props && 
                (orientation === VERTICAL_ORIENTATION && height === undefined && this.props.height !== undefined) ||
                (prevprops.domain[0] !== this.props.domain[0] || prevprops.domain[1] !== this.props.domain[1]))
            this.setState({areaSelector: this.props.areaSelector, colorScheme: this.props.colorScheme, 
                orientation: this.props.orientation, domain: this.props.domain, corner: this.props.corner, height: this.props.height}, 
                () => this._generateDOMElement());
    }

    componentDidMount() {
        this._generateDOMElement();
    }
    
    _generateDOMElement = () => {
        const { areaSelector, colorScheme, orientation, domain, corner, text, height } = this.state;        

        if(domain[0] === undefined || domain[1] === undefined || (orientation === VERTICAL_ORIENTATION && height === undefined))
            return;

        var legendAreaMajorDimensionExtent;
        var legendAreaMinorDimension,legendAreaMajorDimension;//,  legendAreaMinorAxis,  legendAreaMajorAxis;
        let rotation = 0;
        let node = d3.select(`${areaSelector}`).node();

        if (orientation !== VERTICAL_ORIENTATION) {
            //legendAreaMajorAxis = "x";
            //legendAreaMinorAxis = "y";
            legendAreaMinorDimension = "height";
            legendAreaMajorDimension = "width";
            legendAreaMajorDimensionExtent = node === null ? 490 : node.getBoundingClientRect().width - transformX;
        } else {
            //legendAreaMinorAxis = "x";
            //legendAreaMajorAxis = "y";
            legendAreaMinorDimension = "width";
            legendAreaMajorDimension = "height";            
            legendAreaMajorDimensionExtent = height - transformY;
            rotation = 90;
        }

        /*let intMargin = 10;
        let topMargin = 5;*/
        let legendColoration = d3.scaleSequential(colorScheme).domain(domain);
        let legendScale = d3.scaleLinear()
            .range([1, legendAreaMajorDimensionExtent - legendPadding * 2])
            .domain(domain);

        /*let legendPos = width-legendWidth-5;//((nodeWidth-width))-legendWidth-5;
        legendPos = legendPos < 0 ? 0 : legendPos;*/

        
        d3.select(`${areaSelector}`).selectAll("canvas").remove();
        d3.selectAll(`${areaSelector} svg text, ${areaSelector} svg g`).remove();

        let selDivSvg = d3.select(`${areaSelector} svg`)
            .style(legendAreaMajorDimension, legendAreaMajorDimensionExtent)
            .style(legendAreaMinorDimension, LEGEND_SVG_EXTENT+ "px");

        if(corner === SNIP_RIGHT && orientation === VERTICAL_ORIENTATION)
            selDivSvg.attr("transform", `translate(${(LEGEND_BAR_HEIGHT - 2)*2}, 0)`)
        /*.style("position", "absolute")
        .style("top", 0)
        .style("left", 0);*/

        /*let selDivSvg = selDiv.append("svg")
            .attr("class", "legendSvg")
            .attr("height", legendAreaHeight + "px")
            .attr("width", width + "px");
        //.append("g");*/

        var textG = selDivSvg.append("text")
            .attr("dy", "1em")
            .attr("font-size", "smaller")
            .attr("font-weight", "bold")
            .style("text-anchor", "center")
            .text(text);

            let textWidth = textG.node().getComputedTextLength();

        if(orientation === HORIZONTAL_ORIENTATION){
            textG.attr("transform", `translate(${(legendAreaMajorDimensionExtent - textWidth)/2}, -3)`);
        }else{
            if(corner === SNIP_RIGHT)
                textG.attr("transform", `rotate(${rotation}, 0, 0) translate(${(legendAreaMajorDimensionExtent - textWidth)/2}, -${LEGEND_AREA_MINOR_AXIS_EXTENT - 15}) `);
            else
                textG.attr("transform", `rotate(${rotation}, 0, 0) translate(${(legendAreaMajorDimensionExtent - textWidth)/2}, -${LEGEND_AREA_MINOR_AXIS_EXTENT - LEGEND_SVG_EXTENT}) `);
        }

        let ctxNode = d3.select(`${areaSelector}`).append("canvas")
            .attr(legendAreaMinorDimension, 1)
            .attr(legendAreaMajorDimension, legendAreaMajorDimensionExtent - 2*legendPadding)
            .style(legendAreaMajorDimension, legendAreaMajorDimensionExtent  - 2*legendPadding + "px")
            .style(legendAreaMinorDimension, `${LEGEND_BAR_HEIGHT}px`)
            .style("border", "1px solid #000")
            .style("position", "relative")
            .style(orientation === HORIZONTAL_ORIENTATION ? "left" : "bottom", `${legendPadding}px`)
            .style(orientation === HORIZONTAL_ORIENTATION ? "top" : "left", 
                   orientation === HORIZONTAL_ORIENTATION ? "-7px" : 
                   corner === SNIP_LEFT ? "0px" : `-${24}px`)
            .node();
            /*.style("position", "absolute")
            .style("top", "14px")
            .style("left", legendTransformX + "px")
            .node();*/

        let ctx = ctxNode.getContext("2d");

        var image = orientation === HORIZONTAL_ORIENTATION ? 
                    ctx.createImageData(legendAreaMajorDimensionExtent - 2*legendPadding, 1) : ctx.createImageData(1, legendAreaMajorDimensionExtent - 2*legendPadding);
        d3.range(legendAreaMajorDimensionExtent - 2*legendPadding).forEach(function (i) {
            var c = d3.rgb(legendColoration(legendScale.invert(i)));
            image.data[4 * i] = c.r;
            image.data[4 * i + 1] = c.g;
            image.data[4 * i + 2] = c.b;
            image.data[4 * i + 3] = 255;
        });
        ctx.putImageData(image, 0, 0);

        var legendAxis;
        
        switch(corner){
            case SNIP_TOP: legendAxis = d3.axisTop(); break;
            case SNIP_BOTTOM: legendAxis = d3.axisBottom(); break;
            case SNIP_LEFT: legendAxis = d3.axisLeft(); break;
            case SNIP_RIGHT: legendAxis = d3.axisRight(); break;
            default: legendAxis = d3.axisTop(); break;
        }
            
        legendAxis.scale(legendScale);

        legendAxis.ticks(4);

        if(orientation === HORIZONTAL_ORIENTATION)
            selDivSvg.append("g")
                .attr("class", "axis")
                .attr('transform', `translate(${legendPadding}, ${LEGEND_SVG_EXTENT})`)
                .call(legendAxis.tickFormat(d3.format(".1f")));
        else{
            if(corner === SNIP_RIGHT)
                selDivSvg.append("g")
                .attr("class", "axis")
                .attr('transform', `translate(0, ${legendPadding})`)
                .call(legendAxis.tickFormat(d3.format(".1f")))
                .selectAll("text")
                .style("text-anchor", "end")
                .attr("transform", `rotate(-90) translate(0, ${LEGEND_SVG_EXTENT/2 - 4})`);
            else
                selDivSvg.append("g")
                .attr("class", "axis")
                .attr('transform', `translate(${LEGEND_SVG_EXTENT}, ${legendPadding})`)
                .call(legendAxis.tickFormat(d3.format(".1f")))
                .selectAll("text")
                .style("text-anchor", "start")
                .attr("transform", `rotate(90) translate(0, ${LEGEND_SVG_EXTENT/2 - 4})`);
            }

    }

    render() {
        return (<svg></svg>);
    }
}

const ContinuosLegendComponent = (ContinuosLegend);

export default ContinuosLegendComponent;