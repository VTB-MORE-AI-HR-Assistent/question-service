package com.aristurtle.question_service.repository

import com.aristurtle.question_service.model.Skill
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SkillRepository : JpaRepository<Skill, Long> {
    fun findDistinctByTitle(title: String): Optional<Skill>
}