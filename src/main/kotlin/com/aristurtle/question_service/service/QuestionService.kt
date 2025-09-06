package com.aristurtle.question_service.service

import com.aristurtle.question_service.dto.QuestionRequest
import com.aristurtle.question_service.dto.QuestionSearchCriteria
import com.aristurtle.question_service.model.Question
import org.springframework.data.domain.Pageable
import java.util.Optional

interface QuestionService {
    fun getAll(): List<Question>
    fun getAll(pageable: Pageable): List<Question>
    fun getQuestionById(id: Long): Optional<Question>
    fun createQuestion(request: QuestionRequest): Question
    fun updateQuestion(id: Long, request: QuestionRequest): Question
    fun deleteQuestion(id: Long): Boolean
    fun searchQuestions(questionSearchCriteria: QuestionSearchCriteria): List<Question>
    fun searchQuestions(questionSearchCriteria: QuestionSearchCriteria, pageable: Pageable): List<Question>
}