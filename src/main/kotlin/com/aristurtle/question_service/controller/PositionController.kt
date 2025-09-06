package com.aristurtle.question_service.controller

import com.aristurtle.question_service.dto.PositionRequest
import com.aristurtle.question_service.model.Position
import com.aristurtle.question_service.service.PositionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.modelmapper.ModelMapper
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/positions")
@Tag(name = "Positions", description = "API для работы с позициями")
class PositionController(
    private val positionService: PositionService,
    private val modelMapper: ModelMapper
) {
    @GetMapping
    @Operation(
        summary = "Получить все позиции",
        description = "Возвращает список всех позиций с пагинацией",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список позиций",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Position::class)))]
            )
        ]
    )
    fun getAllPositions(
        @Parameter(description = "Номер страницы", example = "0")
        @RequestParam("pageNumber", required = false) pageNumber: Int?,

        @Parameter(description = "Размер страницы", example = "20")
        @RequestParam("pageSize", required = false) pageSize: Int?,
    ): ResponseEntity<List<Position>> {
        val positions = if (pageNumber != null && pageSize != null)
            positionService.getAll(PageRequest.of(pageNumber, pageSize))
        else
            positionService.getAll()
        return ResponseEntity.ok(positions)
    }

    @PostMapping
    @Operation(
        summary = "Создать новую позицию",
        description = "Создает новую позицию и возвращает её",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Позиция успешно создана",
                content = [Content(schema = Schema(implementation = Position::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные позиции"
            )
        ]
    )
    fun createPosition(
        @Parameter(description = "Данные для создания позиции")
        @Valid @RequestBody request: PositionRequest
    ): ResponseEntity<Position> {
        val createdPosition = positionService.createPosition(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPosition)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить позицию по ID",
        description = "Возвращает позицию по указанному идентификатору",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Позиция найдена",
                content = [Content(schema = Schema(implementation = Position::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Позиция не найдена"
            )
        ]
    )
    fun getPositionById(
        @Parameter(description = "ID позиции", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Position> =
        positionService.getPositionById(id)
            .map { modelMapper.map(it, Position::class.java) }
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить позицию",
        description = "Обновляет существующую позицию по ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Позиция успешно обновлена",
                content = [Content(schema = Schema(implementation = Position::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Позиция не найдена"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные позиции"
            )
        ]
    )
    fun updatePosition(
        @Parameter(description = "ID позиции для обновления", example = "1")
        @PathVariable id: Long,

        @Parameter(description = "Данные для обновления позиции")
        @Valid @RequestBody request: PositionRequest
    ): ResponseEntity<Position> {
        return try {
            val updatedPosition = positionService.updatePosition(id, request)
            ResponseEntity.ok(updatedPosition)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить позицию",
        description = "Удаляет позицию по указанному ID",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Позиция успешно удалена"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Позиция не найдена"
            )
        ]
    )
    fun deletePosition(
        @Parameter(description = "ID позиции для удаления", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> =
        if (positionService.deletePosition(id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}