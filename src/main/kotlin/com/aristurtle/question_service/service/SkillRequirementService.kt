package com.aristurtle.question_service.service

import com.aristurtle.question_service.dto.SkillRequirementRequest
import com.aristurtle.question_service.model.SkillRequirement
import org.springframework.data.domain.Pageable
import java.util.*

interface SkillRequirementService {
    fun getAll(): List<SkillRequirement>
    fun getAll(pageable: Pageable): List<SkillRequirement>
    fun getSkillRequirementById(id: Long): Optional<SkillRequirement>
    fun getSkillRequirementsByPositionId(positionId: Long): List<SkillRequirement>
    fun createSkillRequirement(request: SkillRequirementRequest): SkillRequirement
    fun updateSkillRequirement(id: Long, request: SkillRequirementRequest): SkillRequirement
    fun deleteSkillRequirement(id: Long): Boolean
    fun deleteSkillRequirementByPositionAndSkill(positionId: Long, skillId: Long): Boolean
}