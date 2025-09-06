package com.aristurtle.question_service.controller

import com.aristurtle.question_service.dto.SkillRequest
import com.aristurtle.question_service.model.Question
import com.aristurtle.question_service.model.Skill
import com.aristurtle.question_service.service.SkillService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/api/v1/skills")
@Tag(name = "Skills", description = "API для работы с навыками")
class SkillController(
    private val skillService: SkillService
) {

    @GetMapping
    @Operation(
        summary = "Получить все навыки",
        description = "Возвращает список всех навыков с пагинацией",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список навыков",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Skill::class)))]
            )
        ]
    )
    fun getAllSkills(
        @Parameter(description = "Номер страницы", example = "0")
        @RequestParam("pageNumber", required = false) pageNumber: Int?,

        @Parameter(description = "Размер страницы", example = "20")
        @RequestParam("pageSize", required = false) pageSize: Int?,
    ): ResponseEntity<List<Skill>> {
        val skills = if (pageNumber != null && pageSize != null)
            skillService.getAll(PageRequest.of(pageNumber, pageSize))
        else
            skillService.getAll()
        return ResponseEntity.ok(skills)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить навык по ID",
        description = "Возвращает навык по указанному идентификатору",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Навык найден",
                content = [Content(schema = Schema(implementation = Skill::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Навык не найден"
            )
        ]
    )
    fun getSkillById(
        @Parameter(description = "ID навыка", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Skill> =
        skillService.getSkillById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    @Operation(
        summary = "Создать новый навык",
        description = "Создает новый навык и возвращает его",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Навык успешно создан",
                content = [Content(schema = Schema(implementation = Skill::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные навыка"
            )
        ]
    )
    fun createSkill(
        @Parameter(description = "Данные для создания навыка")
        @Valid @RequestBody request: SkillRequest
    ): ResponseEntity<Skill> {
        val createdSkill = skillService.createSkill(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill)
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить навык",
        description = "Обновляет существующий навык по ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Навык успешно обновлен",
                content = [Content(schema = Schema(implementation = Skill::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Навык не найден"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные навыка"
            )
        ]
    )
    fun updateSkill(
        @Parameter(description = "ID навыка для обновления", example = "1")
        @PathVariable id: Long,

        @Parameter(description = "Данные для обновления навыка")
        @Valid @RequestBody request: SkillRequest
    ): ResponseEntity<Skill> {
        return try {
            val updatedSkill = skillService.updateSkill(id, request)
            ResponseEntity.ok(updatedSkill)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить навык",
        description = "Удаляет навык по указанному ID. Если с навыком связаны вопросы, удаление не будет выполнено.",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Навык успешно удален"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Навык не найден"
            ),
            ApiResponse(
                responseCode = "409",
                description = "Нельзя удалить навык, с которым связаны вопросы"
            )
        ]
    )
    fun deleteSkill(
        @Parameter(description = "ID навыка для удаления", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Any> {
        return when (skillService.deleteSkill(id)) {
            SkillService.DeleteResult.SUCCESS -> ResponseEntity.noContent().build()
            SkillService.DeleteResult.NOT_FOUND -> ResponseEntity.notFound().build()
            SkillService.DeleteResult.HAS_QUESTIONS -> ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Skill has related questions")
        }
    }

    @GetMapping("/search")
    @Operation(
        summary = "Поиск навыков по параметрам",
        description = "Возвращает навык подходящий по параметру",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список навыков",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Skill::class)))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Навык не найден"
            )
        ]
    )
    fun findByParameters(
        @Parameter(description = "Уникальное название навыка", example = "Kotlin")
        @RequestParam title: String
    ): ResponseEntity<Any> {
        val skillByTitle = skillService.getSkillByTitle(title)
        return if (skillByTitle.isPresent)
            ResponseEntity.ok(skillByTitle.get())
        else
            ResponseEntity.notFound().build()
    }


    @GetMapping("/{id}/questions")
    @Operation(
        summary = "Получить вопросы по навыку",
        description = "Возвращает все вопросы, связанные с указанным навыком",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список вопросов",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Question::class)))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Навык не найден"
            )
        ]
    )
    fun getQuestionsBySkillId(
        @Parameter(description = "ID навыка", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<List<Question>> {
        return skillService.getQuestionsBySkillId(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }
}