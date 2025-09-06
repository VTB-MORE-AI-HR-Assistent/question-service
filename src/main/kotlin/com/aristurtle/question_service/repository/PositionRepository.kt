package com.aristurtle.question_service.repository

import com.aristurtle.question_service.model.Position
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PositionRepository : JpaRepository<Position, Long> {
    fun findByTitle(title: String): Optional<Position>
    fun existsByTitle(title: String): Boolean
}