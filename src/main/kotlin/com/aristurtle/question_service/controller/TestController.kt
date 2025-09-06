package com.aristurtle.question_service.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test API", description = "Тестовый API для проверки работы")
class TestController {

    @GetMapping
    @Operation(summary = "Тестовый endpoint", description = "Возвращает тестовое сообщение")
    fun test(): Map<String, String> {
        return mapOf("message" to "API работает корректно!")
    }
}