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

import {withStyles} from "@material-ui/core/styles";
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Checkbox from '@material-ui/core/Checkbox';
import AcUnitIcon from '@material-ui/icons/AcUnit';
import { ClickAwayListener } from '@material-ui/core';


const DRAWER_STYLE = {
    root: {
        width: "350px"
    }
};

class SideDrawer extends React.Component{

    constructor(props){
        super(props);
        this.state = {storedOpen: props.open, valueChanged: false};
    }

    componentDidUpdate = () => {
        const{open} = this.props;
        const{storedOpen} = this.state;

        if(storedOpen !== open && open === true)
            this.setState({...this.state, storedOpen: open, valueChanged: false});
    }

    _executeClose = (data = null) => {
        this.setState({storedOpen: false}, () => {
            this._drawerClose(data);
        });
    }

    _drawerClose = (data) => {
        //TO-BE-EXTENDED
    }

    _checkInput = (event) => {
        if (event.type === 'keydown' && event.key === 'Esc') {
            this._executeClose();
          }        
    }
    
    render(){

        const {menuData, classes} = this.props;
        const {storedOpen} = this.state;

        return (
            <div>
                <React.Fragment key='side-menu'>
                        <Drawer anchor='left' open={storedOpen}>
                            <ClickAwayListener onClickAway={() => {if(storedOpen) this._executeClose();}}>
                                <div
                                    className={classes.list}
                                    role="presentation"
                                    onKeyDown={(event) => this._checkInput(event)}
                                    
                                >
                                    {this._generatelist(menuData)}
                                </div>
                            </ClickAwayListener>
                        </Drawer>
                </React.Fragment>
            </div>
          );
    }

}

class GraphsDrawer extends SideDrawer{

    getName = () => {return "graphs";}

    _drawerClose = (data) => {
        const {onUpdatedSelection} = this.props;
        onUpdatedSelection(data);
    }

    _generatelist = (menuData) => {
        return (
                <List>
                    {menuData.length === 0 ? "There are no elements in this list" :
                        menuData.map((data, index) => {
                            const {name, id, vertexCount, edges} = data;
                            return(
                                <ListItem 
                                    divider 
                                    button 
                                    key={id} 
                                    graph_id={id} 
                                    vertexcount={vertexCount} edges={edges} 
                                    name={name}
                                    onClick={(evt) => this._executeClose({graph_id: evt.currentTarget.getAttribute("graph_id")})}>
                                <ListItemIcon><AcUnitIcon /></ListItemIcon>
                                <ListItemText primary={name} secondary={`Vertices: ${vertexCount} - Edges ${edges}`}/>
                                </ListItem>
                            );
                        })
                    }
                </List>
        );
    }
}

class SimulationsDrawer extends SideDrawer{

    getName = () => {return "sims";}

    constructor(props){
        super(props);
        const {loadedSimulations} = this.props;
        let activeSims = 0;
        let initState =         
            props.menuData.map((data) => {
                if(loadedSimulations.get(data.id) !== undefined){
                    activeSims++;
                    return true;
                }
                return false;
            });
        this.state = {...this.state, activeList: initState, valueChanged: false, activeSims: activeSims};
    }

    _drawerClose = () => {
        const {onUpdatedSelection} = this.props;
        const {valueChanged, activeList} = this.state;
        let returnActiveList = [];
        activeList.forEach((d, ind) => {
            if(d)
            returnActiveList.push(ind);
        });
        onUpdatedSelection(valueChanged ? returnActiveList : null);
    }

    _onSelectionUpdate = (currIndex, checked) => {
        const {activeList, activeSims} = this.state;
        if(activeSims < 4 || (activeSims >= 4 && activeList[currIndex])){
            activeList[currIndex] = checked;
            let mod = checked ? 1 : -1;
            this.setState({...this.state, valueChanged: true, activeList: activeList, activeSims: activeSims + mod});
        }
    }

    _generatelist = (menuData) => {
        const {vertexCount} = this.props;
        const {activeList, activeSims} = this.state;
        return (
            <List>
                {menuData.length === 0 ? "There are no elements in this list" :
                    menuData.map((data, index) => {
                        const {id, avg_activated, number_of_seeds, seed_selection, diffusion_strategy} = data;
                        return(
                            <ListItem disabled={activeSims >= 4 && !activeList[index]}
                                button 
                                divider
                                key={id} 
                                //int_index={index} 
                                onClick={(evt) => {
                                    //let currIndex = parseInt(evt.currentTarget.getAttribute("int_index"));
                                    this._onSelectionUpdate(index, !activeList[index])}
                                }
                            >
                                <ListItemIcon>
                                    <Checkbox
                                        edge="start"
                                        checked={activeList[index]}
                                        disabled={activeSims >= 4 && !activeList[index]}
                                        disableRipple
                                        inputProps={{ 'aria-labelledby': index }}
                                        //onClick={(evt) => 
                                        //    this._onSelectionUpdate(index, evt.target.checked)}
                                        //    this.setState({...this.state, valueChanged: true};                                        
                                    />
                            </ListItemIcon>
                            <ListItemText 
                                primary={`Simulation ${id} -- Avg Coverage: ${parseInt((avg_activated/* + number_of_seeds*/)/vertexCount*100)}%`} 
                                secondary={(<span>{seed_selection} - {diffusion_strategy}<br /># of Seeds: {number_of_seeds}</span>)}/>
                            </ListItem>
                        );
                    })
                }
            </List>
        );
    }

}

const GraphSelector = withStyles(DRAWER_STYLE)(GraphsDrawer);
const SimulationsSelector = withStyles(DRAWER_STYLE)(SimulationsDrawer);

export {GraphSelector, SimulationsSelector};