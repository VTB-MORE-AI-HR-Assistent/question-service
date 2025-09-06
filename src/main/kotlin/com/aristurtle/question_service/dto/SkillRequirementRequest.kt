package com.aristurtle.question_service.dto

import com.aristurtle.question_service.exception.BadRequestException
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class SkillRequirementRequest(
    @field:Schema(description = "ID позиции", example = "1", required = true)
    @field:JsonProperty("positionId")
    val positionId: Long,

    @field:Schema(description = "ID навыка", example = "1", required = true)
    @field:JsonProperty("skillId")
    val skillId: Long,

    @field:Schema(description = "Доля легких вопросов (из 100)", example = "20", required = true)
    @field:JsonProperty("easy")
    val easy: Short,

    @field:Schema(description = "Доля средних вопросов (из 100)", example = "50", required = true)
    @field:JsonProperty("medium")
    val medium: Short,

    @field:Schema(description = "Доля сложных вопросов (из 100)", example = "30", required = true)
    @field:JsonProperty("hard")
    val hard: Short
) {
    init {
        require(easy in 0..100) { "Easy must be between 0 and 100" }
        require(medium in 0..100) { "Medium must be between 0 and 100" }
        require(hard in 0..100) { "Hard must be between 0 and 100" }
        if(easy + medium + hard != 100) throw BadRequestException("Sum of the easy, medium, hard must equals 100")
    }
}