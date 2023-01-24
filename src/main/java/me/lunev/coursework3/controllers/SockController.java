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

@RestController
@RequestMapping("api/socks")
@Tag(name = "Носки", description = "CRUD-операции и другие эндпоинты для работы со складом носков")
public class SockController {

    private final SockService sockService;

    private final SocksOperationsService socksOperationsService;

    public SockController(SockService sockService, SocksOperationsService socksOperationsService) {
        this.sockService = sockService;
        this.socksOperationsService = socksOperationsService;
    }

    @Operation(
            summary = "Регистрирует приход товара на склад",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалось добавить приход, суммарный остаток",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
            )
    }
    )
    @PostMapping
    public ResponseEntity<Sock> arrivalSocks(@RequestBody Sock sock) {
        socksOperationsService.addSockOperationArrival(sock);
        return ResponseEntity.ok(sockService.arrivalSocks(sock));
    }

    @Operation(
            summary = "Регистрирует отпуск носков со склада",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта, но общее количество носков" +
                    "указанного цвета и состава не увеличивается, а уменьшается"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Удалось произвести отпуск носков со склада",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
            summary = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса",
            description = "В данном методе количество носков остается неизменным, так как мы запрашиваем информацию о товарах на складе"
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
                    description = "Запрос выполнен, результат в теле ответа в виде строкового представления целого числа",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Параметры запроса отсутствуют или имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
            summary = "Регистрирует списание испорченных (бракованных) носков",
            description = "Параметры запроса передаются в теле запроса в виде JSON-объекта"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Запрос выполнен, товар списан со склада",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Произошла ошибка, не зависящая от вызывающей стороны"
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
