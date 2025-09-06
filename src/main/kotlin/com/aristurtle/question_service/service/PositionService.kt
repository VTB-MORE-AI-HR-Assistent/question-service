package com.aristurtle.question_service.service

import com.aristurtle.question_service.dto.PositionRequest
import com.aristurtle.question_service.model.Position
import org.springframework.data.domain.Pageable
import java.util.*

interface PositionService {
    fun getAll(): List<Position>
    fun getAll(pageable: Pageable): List<Position>
    fun getPositionById(id: Long): Optional<Position>
    fun createPosition(request: PositionRequest): Position
    fun updatePosition(id: Long, request: PositionRequest): Position
    fun deletePosition(id: Long): Boolean
    fun existsByTitle(title: String): Boolean
}