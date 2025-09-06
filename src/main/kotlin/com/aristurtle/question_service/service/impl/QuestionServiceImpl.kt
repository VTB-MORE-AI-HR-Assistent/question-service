package com.aristurtle.question_service.service.impl

import com.aristurtle.question_service.dto.QuestionRequest
import com.aristurtle.question_service.dto.QuestionSearchCriteria
import com.aristurtle.question_service.exception.SkillNotFoundException
import com.aristurtle.question_service.model.DifficultyLevel
import com.aristurtle.question_service.model.Question
import com.aristurtle.question_service.repository.QuestionRepository
import com.aristurtle.question_service.repository.SkillRepository
import com.aristurtle.question_service.service.QuestionService
import com.aristurtle.question_service.util.QuestionSpecifications
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class QuestionServiceImpl(
    private val questionRepository: QuestionRepository,
    private val skillRepository: SkillRepository
) : QuestionService {
    override fun getAll(): List<Question> =
        questionRepository.findAll()

    override fun getAll(pageable: Pageable): List<Question> =
        questionRepository.findAll(pageable).toList()

    override fun getQuestionById(id: Long): Optional<Question> =
        questionRepository.findById(id)

    override fun createQuestion(request: QuestionRequest): Question {
        val skill = skillRepository.findById(request.skillId)
        val question = Question(
            skill = skill.orElseThrow { SkillNotFoundException("Not found skill by id ${request.skillId}") },
            title = request.title,
            rightAnswer = request.rightAnswer,
            difficultyLevel = DifficultyLevel.valueOf(request.difficultyLevel)
        )
        return questionRepository.save(question)
    }

    override fun updateQuestion(
        id: Long,
        request: QuestionRequest
    ): Question {
        val existingQuestion =
            questionRepository.findById(id).orElseThrow { NoSuchElementException("Question with id $id not found") }
        val skill = skillRepository.findById(request.skillId)
        val updatedQuestion = existingQuestion.copy(
            skill = skill.orElseThrow { SkillNotFoundException("Not found skill by id ${request.skillId}") },
            title = request.title,
            rightAnswer = request.rightAnswer,
            difficultyLevel = DifficultyLevel.valueOf(request.difficultyLevel)
        )
        return questionRepository.save(updatedQuestion)
    }

    override fun deleteQuestion(id: Long): Boolean {
        return if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    override fun searchQuestions(questionSearchCriteria: QuestionSearchCriteria): List<Question> {
        val specification = QuestionSpecifications.createSpecification(questionSearchCriteria)
        return questionRepository.findAll(specification)
    }

    override fun searchQuestions(
        questionSearchCriteria: QuestionSearchCriteria,
        pageable: Pageable
    ): List<Question> {
        val specification = QuestionSpecifications.createSpecification(questionSearchCriteria)
        return questionRepository.findAll(specification, pageable).toList()
    }
}