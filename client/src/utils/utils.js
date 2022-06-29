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

export function makeId(length) {
    var result           = '';
    var characters       = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for ( var i = 0; i < length; i++ ) {
       result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
 }

 
export function getSuffixString(value){
	let suffix = [value, ""];
	if (suffix >= 10**12) {
		suffix[0] = (value /(10**12)).toFixed(2);
		suffix[1] = "Tn";
	} else if (value >= 10**9) {
		suffix[0] = (value / (10**9)).toFixed(2);
		suffix[1] = "Bn";
	} else if (value >= 10**6) {
		suffix[0] = (value / (10**6)).toFixed(2);
		suffix[1] = "M";
	} else if (value >= 10**3) {
		suffix[0] = (value / (10**3)).toFixed(2);
		suffix[1] = "K";
	}

	return suffix[0]+suffix[1];
}