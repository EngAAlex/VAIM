/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.24).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package vaim.io.api;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vaim.io.api.structures.SimulationRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-03-17T16:01:30.084Z[GMT]")
@Api(value = "simulate", description = "Initiates the simulation on a graph.")
public interface SimulateApi extends GeneralApi{

	@CrossOrigin(origins = ORIGIN)	
	@ApiOperation(value = "Initiates the simulation on a graph.", nickname = "Simulate")
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Simulation in progress"),        
        @ApiResponse(code = 400, message = "Simulation could not start") })
    @RequestMapping(value = "/simulate",
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<Void> simulate(
    		@NotNull @ApiParam(value = "The graphid to simulate on", required=true) 
    		@Valid @RequestParam(value = "graphId", required = true) Integer graphId, 
    		@ApiParam(value = "The simulation request", required=true) @Valid @RequestBody SimulationRequest body);

}
