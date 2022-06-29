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

import {getSessionID} from './sessionController.js';

const serverRoot = "http://localhost:8088/VAIM/";

export const endpoints = {
    LOAD_AVAILABLE: () => {return {endpoint: "load_available", method: 'GET'}},
    LOAD_GRAPH: (graph_id, resolution) => {
        let session_id = getSessionID();
        let resol = "";
        if(resolution !== undefined)
            resol = `&res=${resolution}`;
        return {endpoint:`load_graph?graph_id=${graph_id}&session_id=${session_id}${resol}`, method: 'GET'}},
    LOAD_SIMULATION_FRAMES: (full_delivery = true) => {
        let session_id = getSessionID();
        return {endpoint:`get_simulation_frames?session_id=${session_id}&full_delivery=${full_delivery}`, method: 'POST'}},
    GET_SECTORS_DRAWING: () => {
        let session_id = getSessionID();
        return {endpoint: `get_sector_drawing?session_id=${session_id}`, method: 'POST'}
    },
    UPDATE_RESOLUTION: (newRes) => {
        let session_id = getSessionID();
        return {endpoint: `update_drawing_resolution?session_id=${session_id}&newresolution=${newRes}`, method: 'GET'}        
    },
    REQUEST_SIMULATION: (graphId) => {
        return {endpoint: `simulate?graphId=${graphId}`, method: 'POST'}        
    } 
}

export async function executeFetch(requestData, data = ""){
    try {
        let opts = {
            method: requestData.method,
            headers: {
            'Content-Type': 'application/json',
            }};
        if(requestData.method === "POST")
            opts.body = JSON.stringify(data);
        const response = await fetch(
            `${serverRoot}${requestData.endpoint}`
            ,opts);
        if (response.status !== 200) throw Error(response.json().message);
        try{       
            const fetchedData = await response.json();
            return {fetchedData};
        }catch (jsonErr){
            return {};
        }
    } catch (err) {
        console.log(err.message, err.stack);
        return {err};
    }

}