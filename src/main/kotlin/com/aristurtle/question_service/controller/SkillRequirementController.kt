package com.aristurtle.question_service.controller

import com.aristurtle.question_service.dto.SkillRequirementRequest
import com.aristurtle.question_service.model.SkillRequirement
import com.aristurtle.question_service.service.SkillRequirementService
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
@RequestMapping("/api/v1/skill-requirements")
@Tag(name = "Skill Requirements", description = "API для работы с требованиями к навыкам")
class SkillRequirementController(
    private val skillRequirementService: SkillRequirementService
) {
    @GetMapping
    @Operation(
        summary = "Получить все требования",
        description = "Возвращает список всех требований к навыкам с пагинацией",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список требований",
                content = [Content(array = ArraySchema(schema = Schema(implementation = SkillRequirement::class)))]
            )
        ]
    )
    fun getAllSkillRequirements(
        @Parameter(description = "Номер страницы", example = "0")
        @RequestParam("pageNumber", required = false) pageNumber: Int?,

        @Parameter(description = "Размер страницы", example = "20")
        @RequestParam("pageSize", required = false) pageSize: Int?,
    ): ResponseEntity<List<SkillRequirement>> {
        val requirements = if (pageNumber != null && pageSize != null)
            skillRequirementService.getAll(PageRequest.of(pageNumber, pageSize))
        else
            skillRequirementService.getAll()
        return ResponseEntity.ok(requirements)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить требование по ID",
        description = "Возвращает требование по указанному идентификатору",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Требование найдено",
                content = [Content(schema = Schema(implementation = SkillRequirement::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Требование не найдено"
            )
        ]
    )
    fun getSkillRequirementById(
        @Parameter(description = "ID требования", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<SkillRequirement> =
        skillRequirementService.getSkillRequirementById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @GetMapping("/search")
    @Operation(
        summary = "Получить требования по позиции",
        description = "Возвращает все требования для указанной позиции",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список требований",
                content = [Content(array = ArraySchema(schema = Schema(implementation = SkillRequirement::class)))]
            )
        ]
    )
    fun search(
        @Parameter(description = "ID позиции", example = "1")
        @RequestParam positionId: Long
    ): ResponseEntity<List<SkillRequirement>> {
        val requirements = skillRequirementService.getSkillRequirementsByPositionId(positionId)
        return ResponseEntity.ok(requirements)
    }

    @PostMapping
    @Operation(
        summary = "Создать новое требование",
        description = "Создает новое требование к навыку для позиции",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Требование успешно создано",
                content = [Content(schema = Schema(implementation = SkillRequirementRequest::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные требования"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Позиция или навык не найдены"
            )
        ]
    )
    fun createSkillRequirement(
        @Parameter(description = "Данные для создания требования")
        @Valid @RequestBody request: SkillRequirementRequest
    ): ResponseEntity<Any> {
        return try {
            val createdRequirement = skillRequirementService.createSkillRequirement(request)
            ResponseEntity.status(HttpStatus.CREATED).body(createdRequirement)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить требование",
        description = "Обновляет существующее требование по ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Требование успешно обновлено",
                content = [Content(schema = Schema(implementation = SkillRequirement::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Требование не найдено"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные требования"
            )
        ]
    )
    fun updateSkillRequirement(
        @Parameter(description = "ID требования для обновления", example = "1")
        @PathVariable id: Long,

        @Parameter(description = "Данные для обновления требования")
        @Valid @RequestBody request: SkillRequirementRequest
    ): ResponseEntity<Any> {
        return try {
            val updatedRequirement = skillRequirementService.updateSkillRequirement(id, request)
            ResponseEntity.ok(updatedRequirement)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить требование",
        description = "Удаляет требование по указанному ID",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Требование успешно удалено"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Требование не найдено"
            )
        ]
    )
    fun deleteSkillRequirement(
        @Parameter(description = "ID требования для удаления", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> =
        if (skillRequirementService.deleteSkillRequirement(id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()

    @DeleteMapping("/position/{positionId}/skill/{skillId}")
    @Operation(
        summary = "Удалить требование по позиции и навыку",
        description = "Удаляет требование по комбинации позиции и навыка",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Требование успешно удалено"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Требование не найдено"
            )
        ]
    )
    fun deleteSkillRequirementByPositionAndSkill(
        @Parameter(description = "ID позиции", example = "1")
        @PathVariable positionId: Long,

        @Parameter(description = "ID навыка", example = "1")
        @PathVariable skillId: Long
    ): ResponseEntity<Void> =
        if (skillRequirementService.deleteSkillRequirementByPositionAndSkill(positionId, skillId))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()
}