package com.aristurtle.question_service.dto

import com.aristurtle.question_service.model.DifficultyLevel
import com.aristurtle.question_service.model.Skill

data class QuestionSearchCriteria(
    val skill: Skill?,
    val title: String?,
    val rightAnswer: String?,
    val difficultyLevel: DifficultyLevel?
)