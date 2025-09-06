package com.aristurtle.question_service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

data class QuestionRequest(
    @field:Schema(description = "Id навыка", example = "1", required = true)
    @field:JsonProperty("skillId")
    val skillId: Long,
    
    @field:Schema(description = "Название вопроса", example = "Что такое переменная в Kotlin?", required = true)
    @field:JsonProperty("title")
    val title: String,
    
    @field:Schema(description = "Правильный ответ", example = "val или var", required = false)
    @field:JsonProperty("rightAnswer")
    val rightAnswer: String?,
    
    @field:Schema(description = "Уровень сложности", example = "EASY", required = true)
    @field:Pattern(
        regexp = "EASY|MEDIUM|HARD",
        message = "Допустимые значения: EASY, MEDIUM, HARD",
        flags = [Pattern.Flag.CASE_INSENSITIVE]
    )
    @field:JsonProperty("difficultyLevel")
    val difficultyLevel: String
)