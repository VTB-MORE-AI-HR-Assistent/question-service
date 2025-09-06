package com.aristurtle.question_service.service.impl

import com.aristurtle.question_service.dto.SkillRequirementRequest
import com.aristurtle.question_service.model.SkillRequirement
import com.aristurtle.question_service.repository.SkillRequirementRepository
import com.aristurtle.question_service.repository.PositionRepository
import com.aristurtle.question_service.repository.SkillRepository
import com.aristurtle.question_service.service.SkillRequirementService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SkillRequirementServiceImpl(
    private val skillRequirementRepository: SkillRequirementRepository,
    private val positionRepository: PositionRepository,
    private val skillRepository: SkillRepository
) : SkillRequirementService {

    override fun getAll(): List<SkillRequirement> =
        skillRequirementRepository.findAll()

    override fun getAll(pageable: Pageable): List<SkillRequirement> =
        skillRequirementRepository.findAll(pageable).toList()

    override fun getSkillRequirementById(id: Long): Optional<SkillRequirement> =
        skillRequirementRepository.findById(id)

    @Transactional(readOnly = true)
    override fun getSkillRequirementsByPositionId(positionId: Long): List<SkillRequirement> =
        skillRequirementRepository.findByPositionId(positionId)

    override fun createSkillRequirement(request: SkillRequirementRequest): SkillRequirement {
        val position = positionRepository.findById(request.positionId)
            .orElseThrow { NoSuchElementException("Position with id ${request.positionId} not found") }
        
        val skill = skillRepository.findById(request.skillId)
            .orElseThrow { NoSuchElementException("Skill with id ${request.skillId} not found") }

        if (skillRequirementRepository.existsByPositionIdAndSkillId(request.positionId, request.skillId)) {
            throw IllegalArgumentException("Skill requirement for this position and skill already exists")
        }

        val requirement = SkillRequirement(
            position = position,
            skill = skill,
            easy = request.easy,
            medium = request.medium,
            hard = request.hard
        )

        return skillRequirementRepository.save(requirement)
    }

    override fun updateSkillRequirement(id: Long, request: SkillRequirementRequest): SkillRequirement {
        val existingRequirement = skillRequirementRepository.findById(id)
            .orElseThrow { NoSuchElementException("Skill requirement with id $id not found") }

        val position = positionRepository.findById(request.positionId)
            .orElseThrow { NoSuchElementException("Position with id ${request.positionId} not found") }
        
        val skill = skillRepository.findById(request.skillId)
            .orElseThrow { NoSuchElementException("Skill with id ${request.skillId} not found") }

        // Проверяем, не конфликтует ли обновление с другими требованиями
        if (existingRequirement.position.id != request.positionId || 
            existingRequirement.skill.id != request.skillId) {
            if (skillRequirementRepository.existsByPositionIdAndSkillId(request.positionId, request.skillId)) {
                throw IllegalArgumentException("Skill requirement for this position and skill already exists")
            }
        }

        val updatedRequirement = existingRequirement.copy(
            position = position,
            skill = skill,
            easy = request.easy,
            medium = request.medium,
            hard = request.hard
        )

        return skillRequirementRepository.save(updatedRequirement)
    }

    override fun deleteSkillRequirement(id: Long): Boolean {
        return if (skillRequirementRepository.existsById(id)) {
            skillRequirementRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    override fun deleteSkillRequirementByPositionAndSkill(positionId: Long, skillId: Long): Boolean {
        val requirement = skillRequirementRepository.findByPositionIdAndSkillId(positionId, skillId)
        return if (requirement.isPresent) {
            skillRequirementRepository.delete(requirement.get())
            true
        } else {
            false
        }
    }
}