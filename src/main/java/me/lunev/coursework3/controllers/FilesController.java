package me.lunev.coursework3.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lunev.coursework3.model.Sock;
import me.lunev.coursework3.services.FilesService;
import me.lunev.coursework3.services.SockService;
import me.lunev.coursework3.services.SocksOperationsService;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Class - controller for working with files, containing a set of API endpoints
 *
 * @see FilesService
 * @see SockService
 * @see SocksOperationsService
 */
@RestController
@RequestMapping("/files")
@Tag(name = "Socks - files", description = "CRUD operations and other endpoints for working with sock warehouse files")
public class FilesController {

    private final FilesService filesService;

    private final SockService sockService;

    private final SocksOperationsService socksOperationsService;

    public FilesController(FilesService filesService, SockService sockService, SocksOperationsService socksOperationsService) {
        this.filesService = filesService;
        this.sockService = sockService;
        this.socksOperationsService = socksOperationsService;
    }

    @Operation(
            summary = "Export data in current state",
            description = "Endopoint generates JSON from data in memory, writes it to a file and unloads it on request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, file available for download",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "File not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @GetMapping(value = "/export")
    public ResponseEntity<InputStreamResource> downloadSocksFile() throws FileNotFoundException {
        File file = filesService.getSocksFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(
            summary = "Data import",
            description = "The endpoint accepts a json file with data as input and replaces the data in memory with them"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, data from file imported",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "File not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadSocksFile(@RequestParam MultipartFile file) {
        filesService.cleanSocksFile();
        File socksFile = filesService.getSocksFile();
        try (FileOutputStream fos = new FileOutputStream(socksFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            sockService.readSocksFromFile();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(
            summary = "Export operations of receiving and issuing socks",
            description = "Endopoint generates a history of operations for receiving and issuing socks from data " +
                    "in JSON memory, writes it to a file and uploads it on request"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, file available for download",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "File not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @GetMapping(value = "/export/operations")
    public ResponseEntity<InputStreamResource> downloadSocksOperationsFile() throws FileNotFoundException {
        File file = filesService.getSocksOperationsFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"SocksOperationsLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(
            summary = "Import operations of receiving and issuing socks",
            description = "The endpoint accepts a json file with data as input and replaces the data in memory with them"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Request completed, data from file imported",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "File not found"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "An error occurred that is not dependent on the caller"
            )
    }
    )
    @PostMapping(value = "/import/operations", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadSocksOperationsFile(@RequestParam MultipartFile file) {
        filesService.cleanSocksOperationsFile();
        File socksOperationsFile = filesService.getSocksOperationsFile();
        try (FileOutputStream fos = new FileOutputStream(socksOperationsFile)) {
            IOUtils.copy(file.getInputStream(), fos);

            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
