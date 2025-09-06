package com.aristurtle.question_service.controller

import com.aristurtle.question_service.dto.QuestionRequest
import com.aristurtle.question_service.dto.QuestionSearchCriteria
import com.aristurtle.question_service.exception.SkillNotFoundException
import com.aristurtle.question_service.model.Question
import com.aristurtle.question_service.service.QuestionService
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
@RequestMapping("/api/v1/questions")
@Tag(name = "Questions", description = "API для работы с вопросами")
class QuestionController(
    private val questionService: QuestionService,
    private val modelMapper: ModelMapper
) {

    @GetMapping
    @Operation(
        summary = "Получить все вопросы",
        description = "Возвращает список всех вопросов с пагинацией",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список вопросов",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Question::class)))]
            )
        ]
    )
    fun getAllQuestions(
        @Parameter(description = "Номер страницы", example = "0")
        @RequestParam("pageNumber", required = false) pageNumber: Int?,

        @Parameter(description = "Размер страницы", example = "20")
        @RequestParam("pageSize", required = false) pageSize: Int?,
    ): ResponseEntity<List<Question>> {
        val questions = if (pageNumber != null && pageSize != null)
            questionService.getAll(PageRequest.of(pageNumber, pageSize))
        else
            questionService.getAll()
        return ResponseEntity.ok(questions)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить вопрос по ID",
        description = "Возвращает вопрос по указанному идентификатору",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Вопрос найден",
                content = [Content(schema = Schema(implementation = Question::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Вопрос не найден"
            )
        ]
    )
    fun getQuestionById(
        @Parameter(description = "ID вопроса", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Question> =
        questionService.getQuestionById(id)
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())

    @PostMapping
    @Operation(
        summary = "Создать новый вопрос",
        description = "Создает новый вопрос и возвращает его. Если навыка не существует, он будет создан автоматически.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Вопрос успешно создан",
                content = [Content(schema = Schema(implementation = QuestionRequest::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные вопроса"
            )
        ]
    )
    fun createQuestion(
        @Parameter(description = "Данные для создания вопроса")
        @Valid @RequestBody request: QuestionRequest
    ): ResponseEntity<Any> {
        return try{
            val createdQuestion = questionService.createQuestion(request)
            ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion)
        } catch (e: SkillNotFoundException) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Обновить вопрос",
        description = "Обновляет существующий вопрос по ID. Если передан новый навык, он будет создан автоматически.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Вопрос успешно обновлен",
                content = [Content(schema = Schema(implementation = QuestionRequest::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Вопрос не найден"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные данные вопроса"
            )
        ]
    )
    fun updateQuestion(
        @Parameter(description = "ID вопроса для обновления", example = "1")
        @PathVariable id: Long,

        @Parameter(description = "Данные для обновления вопроса")
        @Valid @RequestBody request: QuestionRequest
    ): ResponseEntity<Any> {
        return try {
            val updatedQuestion = questionService.updateQuestion(id, request)
            return ResponseEntity.ok(updatedQuestion)
        } catch (e: SkillNotFoundException){
            ResponseEntity.badRequest().body(e.message)
        } catch (e: NoSuchElementException) {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Удалить вопрос",
        description = "Удаляет вопрос по указанному ID",
        responses = [
            ApiResponse(
                responseCode = "204",
                description = "Вопрос успешно удален"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Вопрос не найден"
            )
        ]
    )
    fun deleteQuestion(
        @Parameter(description = "ID вопроса для удаления", example = "1")
        @PathVariable id: Long
    ): ResponseEntity<Void> =
        if (questionService.deleteQuestion(id))
            ResponseEntity.noContent().build()
        else
            ResponseEntity.notFound().build()


    @PostMapping("/search")
    @Operation(
        summary = "Поиск вопросов по критериям",
        description = "Поиск вопросов с фильтрацией по навыку, названию, правильному ответу и уровню сложности",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Список найденных вопросов",
                content = [Content(array = ArraySchema(schema = Schema(implementation = Question::class)))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Неверные параметры запроса"
            )
        ]
    )
    fun searchQuestions(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Поисковый объект",
            required = true,
            content = [Content(schema = Schema(implementation = QuestionSearchCriteria::class))]
        )
        @RequestBody questionSearchCriteria: QuestionSearchCriteria,

        @Parameter(name = "pageNumber", description = "Параметр пагинации: номер начальной страницы", required = false)
        @RequestParam("pageNumber", required = false) pageNumber: Int?,

        @Parameter(name = "pageSize", description = "Параметр пагинации: объем возвращаемых страниц", required = false)
        @RequestParam("pageSize", required = false) pageSize: Int?,
    ): ResponseEntity<List<Question>> {
        val questions = if (pageNumber != null && pageSize != null)
            questionService.searchQuestions(
                questionSearchCriteria = questionSearchCriteria,
                pageable =  PageRequest.of(pageNumber, pageSize))
        else
            questionService.searchQuestions(questionSearchCriteria)
        return ResponseEntity.ok(questions)
    }
}