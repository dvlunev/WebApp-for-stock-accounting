package me.lunev.coursework3.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@RestController
@RequestMapping("/files")
@Tag(name = "Носки - файлы", description = "CRUD-операции и другие эндпоинты для работы c файлами склада носков")
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
            summary = "Экспорт данных в текущем состоянии",
            description = "Эндопоинт формирует из данных в памяти JSON, записывает его в файл и выгружает по запросу"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, файл доступен для скачивания",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл не найден"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"SocksLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(
            summary = "Импорт данных",
            description = "Эндпоинт принимает на вход json-файл с данными и заменяет ими данные в памяти"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, данные из файла импортированы",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл не найден"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
            summary = "Экспорт операций приемки и выдачи носков",
            description = "Эндопоинт формирует историю операций приемки и выдачи носков из данных в памяти JSON," +
                    "записывает его в файл и выгружает по запросу"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, файл доступен для скачивания",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл не найден"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    @GetMapping(value = "/export/operations")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy HH:mm:ss")
    public ResponseEntity<InputStreamResource> downloadSocksOperationsFile() throws FileNotFoundException {
        File file = filesService.getSocksOperationsFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"SocksOperationsLog.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(
            summary = "Импорт операций приемки и выдачи носков",
            description = "Эндпоинт принимает на вход json-файл с данными и заменяет ими данные в памяти"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, данные из файла импортированы",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Файл не найден"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
