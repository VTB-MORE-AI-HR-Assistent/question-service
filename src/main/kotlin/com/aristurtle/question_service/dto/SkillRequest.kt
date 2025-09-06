package com.aristurtle.question_service.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class SkillRequest(
    @field:Schema(description = "Название навыка", example = "Kotlin Basics", required = true)
    @field:JsonProperty("title")
    val title: String
)