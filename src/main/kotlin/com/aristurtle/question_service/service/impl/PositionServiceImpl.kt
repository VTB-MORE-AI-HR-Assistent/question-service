package com.aristurtle.question_service.service.impl

import com.aristurtle.question_service.dto.PositionRequest
import com.aristurtle.question_service.model.Position
import com.aristurtle.question_service.repository.PositionRepository
import com.aristurtle.question_service.service.PositionService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class PositionServiceImpl(
    private val positionRepository: PositionRepository
) : PositionService {

    override fun getAll(): List<Position> =
        positionRepository.findAll()

    override fun getAll(pageable: Pageable): List<Position> =
        positionRepository.findAll(pageable).toList()

    override fun getPositionById(id: Long): Optional<Position> =
        positionRepository.findById(id)

    override fun createPosition(request: PositionRequest): Position {
        val position = Position(title = request.title)
        return positionRepository.save(position)
    }

    override fun updatePosition(id: Long, request: PositionRequest): Position {
        val existingPosition = positionRepository.findById(id)
            .orElseThrow { NoSuchElementException("Position with id $id not found") }
        val updatedPosition = existingPosition.copy(title = request.title)
        return positionRepository.save(updatedPosition)
    }

    override fun deletePosition(id: Long): Boolean {
        return if (positionRepository.existsById(id)) {
            positionRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    override fun existsByTitle(title: String): Boolean =
        positionRepository.existsByTitle(title)
}