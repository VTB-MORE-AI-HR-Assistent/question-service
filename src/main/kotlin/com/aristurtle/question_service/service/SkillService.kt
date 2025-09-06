package com.aristurtle.question_service.service

import com.aristurtle.question_service.dto.SkillRequest
import com.aristurtle.question_service.model.Skill
import com.aristurtle.question_service.model.Question
import org.springframework.data.domain.Pageable
import java.util.*

interface SkillService {
    enum class DeleteResult {
        SUCCESS, NOT_FOUND, HAS_QUESTIONS
    }

    fun getAll(): List<Skill>
    fun getAll(pageable: Pageable): List<Skill>
    fun getSkillById(id: Long): Optional<Skill>
    fun getSkillByTitle(title: String): Optional<Skill>
    fun createSkill(request: SkillRequest): Skill
    fun updateSkill(id: Long, request: SkillRequest): Skill
    fun deleteSkill(id: Long): DeleteResult
    fun getQuestionsBySkillId(skillId: Long): Optional<List<Question>>
}