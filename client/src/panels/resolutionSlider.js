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

import React, { Component } from 'react';

import Typography from '@material-ui/core/Typography';
import { withStyles } from "@material-ui/core/styles";
import ZoomInIcon from '@material-ui/icons/ZoomIn'
import ZoomOutIcon from '@material-ui/icons/ZoomOut'
import Slider from '@material-ui/core/Slider';
import Button from '@material-ui/core/Button';

export const SLIDER_STYLES = {    
    sliderAreaRoot: {
        display: "inline-block",
            width: "100%",
                textAlign: "center",
                    padding: "5px"
    },
    buttonTable: {
        textAlign: "center",
        width: "100%",
    },
    button: {
        border: "solid 1px lightgrey",
        borderRadius: "4px"
    },
    matrixSlider: {
        width: "98%",
            marginLeft: "1%",
                border: "solid 0px lightgray",
                    borderRadius: "19px",
                        background: "rgba(0,0,0,0.1)"
    }
}

class ResolutionSlider extends Component {

    constructor(props) {
        const {defaultResolution, resolutionsArray} = props;
        super(props);
        this.state = {min: 0, max: resolutionsArray.length - 1, resolutionsArray: resolutionsArray,
             storedResolution: defaultResolution, defaultResolution: defaultResolution}
    }

    componentDidMount(){}

    componentDidUpdate(){}

    render() {        
        const {defaultResolution, min, max, resolutionsArray, storedResolution} = this.state;
        const {classes, enabled} = this.props;

        if(!enabled)
            return "";

        return (
            <div className={classes.sliderAreaRoot}>
                <table className={classes.buttonTable}>
                    <thead></thead>
                    <tbody>
                        <tr>
                            <td><Button className={classes.button} onClick={() => this._updateResolution(storedResolution - 1)} disabled={storedResolution === 0}>
                                <ZoomOutIcon /></Button></td>
                            <td><Typography>Resolution: {resolutionsArray[storedResolution]} × {resolutionsArray[storedResolution]}</Typography></td>
                            <td><Button className={classes.button} onClick={() => this._updateResolution(storedResolution + 1)} disabled={storedResolution === resolutionsArray.length - 1}>
                                <ZoomInIcon /></Button></td>
                        </tr>
                    </tbody>
                </table>
                <div className={classes.matrixSlider}>
                    <Slider
                        defaultValue={defaultResolution}
                        aria-labelledby="discrete-slider"
                        valueLabelDisplay="auto"
                        style={{ width: "91%" }}
                        marks={true}
                        step={1}
                        min={min}
                        max={max}
                        value={storedResolution}
                        onChangeCommitted={(e, value) => this._updateResolution(value)}
                    />
                </div>
            </div>
        );
    }

    _updateResolution = (value) => {
        const {onResolutionChanged} = this.props;
        this.setState({...this.state, storedResolution: value}, () => onResolutionChanged(value))
    }
}

const ResolutionSliderComponent = withStyles(SLIDER_STYLES)(ResolutionSlider);

export default ResolutionSliderComponent;