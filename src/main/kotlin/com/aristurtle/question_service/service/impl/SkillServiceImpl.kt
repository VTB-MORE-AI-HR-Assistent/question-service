package com.aristurtle.question_service.service.impl

import com.aristurtle.question_service.dto.SkillRequest
import com.aristurtle.question_service.model.Question
import com.aristurtle.question_service.model.Skill
import com.aristurtle.question_service.repository.QuestionRepository
import com.aristurtle.question_service.repository.SkillRepository
import com.aristurtle.question_service.service.SkillService
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class SkillServiceImpl(
    private val skillRepository: SkillRepository,
    private val questionRepository: QuestionRepository
) : SkillService {

    @Transactional(readOnly = true)
    override fun getAll(): List<Skill> =
        skillRepository.findAll()

    @Transactional(readOnly = true)
    override fun getAll(pageable: Pageable): List<Skill> =
        skillRepository.findAll(pageable).toList()

    @Transactional(readOnly = true)
    override fun getSkillById(id: Long): Optional<Skill> =
        skillRepository.findById(id)

    @Transactional(readOnly = true)
    override fun getSkillByTitle(title: String): Optional<Skill> =
        skillRepository.findDistinctByTitle(title)

    override fun createSkill(request: SkillRequest): Skill {
        val skill = Skill(title = request.title)
        return skillRepository.save(skill)
    }

    override fun updateSkill(id: Long, request: SkillRequest): Skill {
        val existingSkill = skillRepository.findById(id)
            .orElseThrow { NoSuchElementException("Skill with id $id not found") }
        val updatedSkill = existingSkill.copy(title = request.title)
        return skillRepository.save(updatedSkill)
    }

    override fun deleteSkill(id: Long): SkillService.DeleteResult {
        return if (skillRepository.existsById(id)) {
            val questionCount = questionRepository.countBySkillId(id)
            if (questionCount > 0) {
                SkillService.DeleteResult.HAS_QUESTIONS
            } else {
                skillRepository.deleteById(id)
                SkillService.DeleteResult.SUCCESS
            }
        } else {
            SkillService.DeleteResult.NOT_FOUND
        }
    }

    override fun getQuestionsBySkillId(skillId: Long): Optional<List<Question>> {
        return if (!skillRepository.existsById(skillId))
            Optional.empty<List<Question>>()
        else
            questionRepository.findBySkillId(skillId)
                .let { Optional.of(it) }
    }
}