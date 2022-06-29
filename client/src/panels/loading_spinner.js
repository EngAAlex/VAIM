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
import {withStyles} from "@material-ui/core/styles";
import {Typography, Modal, Paper} from "@material-ui/core";

const LOADER_CLASSES = {
    paper: {
        //position: 'absolute',
        backgroundColor: "white",
        //borderRadius: "12px",
        width: '500px',
        height: '220px',
        //border: '2px solid #000',
        boxShadow: '3px',
        padding: '5px',
    },
    container:{
        //position: 'absolute',
        //left: '50%',
        //top: '50%',
        //zIndex: 10,
        //backgroundColor: "white",
        //borderRadius: "12px",
        width: '500px',
        height: '220px',
        margin: 'auto'
        //margin: '-110px 0 0 -250px',
    },
    loader: {
        position: 'absolute',
        top: '5px',
        zIndex: 10,
        width: '150px',
        height: '150px',
        margin: '0px 0 0 159px',
        //border: '16px solid #f3f3f3',
        borderRadius: '50%',
        borderTop: '16px solid #3498db',
        animation: '$spinAnimation 3s linear infinite',
    },
    '@keyframes spinAnimation': {
        '0%': { transform: 'rotate(0deg)' },
        '100%': { transform: 'rotate(360deg)' },
    },
    loaderLabel: {
        bottom: '3%',
        textAlign: 'center',
        //left: '50%',
        width: '100%',//'150px',
        //height: '150px',
        margin: '-75px 0 0 0',
        zIndex: '10',
        position: 'absolute',
        color: 'black'
    },
    backdrop: {
        background: 'rgba(255,255,255,0.2) !important',
      },
};

class LoadingSpinner extends Component{

    constructor(props){
        super(props)
        this.state = {};
    }

    render(){
        const {active, loading_label, classes} = this.props;
        /*if(!active)
            return (<div style={{display: "none"}}></div>);*/
        return (
                <Modal className={classes.container}
                    open={active}
                    aria-labelledby="simple-modal-title"
                    aria-describedby="simple-modal-description"
                    //onClose={(d) => onClose(d)}
                    disableEscapeKeyDown={true}
                    disableBackdropClick={true}
                    BackdropProps= {{
                        className: classes.backdrop                        
                    }}
                >
                    <Paper variant="elevation" elevation={15} className={classes.paper}>
                        <div className={classes.loader}></div>
                        <Typography className={classes.loaderLabel}>{loading_label}</Typography>                    
                    </Paper>
                </Modal>                
        );
    }

}

const LoadingAnimation = withStyles(LOADER_CLASSES)(LoadingSpinner);
export {LoadingAnimation};