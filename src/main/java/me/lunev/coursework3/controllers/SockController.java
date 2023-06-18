package me.lunev.coursework3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lunev.coursework3.model.Sock;
import me.lunev.coursework3.services.SockService;
import me.lunev.coursework3.services.SocksOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Class - controller for working with socks, containing a set of API endpoints
 *
 * @see SockService
 * @see SocksOperationsService
 */
@RestController
@RequestMapping("api/socks")
@Tag(name = "Socks", description = "CRUD operations and other endpoints for working with the stock of socks")
public class SockController {

    private final SockService sockService;

    private final SocksOperationsService socksOperationsService;

    public SockController(SockService sockService, SocksOperationsService socksOperationsService) {
        this.sockService = sockService;
        this.socksOperationsService = socksOperationsService;
    }

    @Operation(
            summary = "Registers the arrival of goods at the warehouse",
            description = "Request parameters are passed in the request body as a JSON object"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Income added, total balance",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameters are missing or incorrectly formatted"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @PostMapping
    public ResponseEntity<Sock> arrivalSocks(@RequestBody Sock sock) {
        socksOperationsService.addSockOperationArrival(sock);
        return ResponseEntity.ok(sockService.arrivalSocks(sock));
    }

    @Operation(
            summary = "Registers the release of socks from the warehouse",
            description = "The request parameters are passed in the request body as a JSON object, but " +
                    "the total number of socks of the specified color and composition does not increase, but decreases"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Socks issued from the warehouse",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The product is not in stock in the required quantity or the request parameters are not in the correct format"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @PutMapping
    public ResponseEntity<Sock> deliverySocks(@RequestBody Sock sock) {
        Sock newSock = sockService.deliveryOrMinusSocks(sock);
        if (newSock == null) {
            return ResponseEntity.notFound().build();
        }
        socksOperationsService.addSockOperationDelivery(sock);
        return ResponseEntity.ok(newSock);
    }

    @Operation(
            summary = "Returns the total number of socks in stock that match the query criteria passed in the parameters",
            description = "In this method, the number of socks remains the same as we are requesting information about the items in stock."
    )
    @Parameters(value = {
            @Parameter(name = "color", example = "red"),
            @Parameter(name = "size", example = "36"),
            @Parameter(name = "cottonMin", example = "0"),
            @Parameter(name = "cottonMax", example = "100")
    }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, result in response body as a string representation of an integer",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Query parameters are missing or incorrectly formatted"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @GetMapping
    public ResponseEntity<Integer> getSumSocks(@RequestParam String color,
                                               @RequestParam float size,
                                               @RequestParam(required = false) Integer cottonMin,
                                               @RequestParam(required = false) Integer cottonMax) {
        if (sockService.getSumSocks(color, size, cottonMin, cottonMax) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sockService.getSumSocks(color, size, cottonMin, cottonMax));
    }

    @Operation(
            summary = "Registers the write-off of damaged (defective) socks",
            description = "Request parameters are passed in the request body as a JSON object"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, item removed from stock",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "The product is not in stock in the required quantity or the request parameters are not in the correct format"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @DeleteMapping
    public ResponseEntity<Sock> minusSocks(@RequestBody Sock sock) {
        Sock newSock = sockService.deliveryOrMinusSocks(sock);
        if (newSock == null) {
            return ResponseEntity.notFound().build();
        }
        socksOperationsService.addSockOperationMinus(sock);
        return ResponseEntity.ok(newSock);
    }
}
