package com.aristurtle.question_service.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PositionRequest(
    @field:Schema(description = "Название позиции", example = "Junior Backend Developer", required = true)
    val title: String
)