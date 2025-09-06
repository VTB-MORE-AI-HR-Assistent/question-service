package com.aristurtle.question_service.util

import com.aristurtle.question_service.dto.QuestionSearchCriteria
import com.aristurtle.question_service.model.DifficultyLevel
import com.aristurtle.question_service.model.Question
import com.aristurtle.question_service.model.Skill
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.Predicate

object QuestionSpecifications {

    fun hasSkill(skill: Skill?): Specification<Question>? {
        return skill?.let {
            Specification { root, _, builder ->
                builder.equal(root.get<Skill>("skill"), skill)
            }
        }
    }

    fun containsTitle(title: String?): Specification<Question>? {
        return title?.takeIf { it.isNotBlank() }?.let {
            Specification { root, _, builder ->
                builder.like(builder.lower(root.get<String>("title")), "%${title.lowercase()}%")
            }
        }
    }

    fun hasRightAnswer(rightAnswer: String?): Specification<Question>? {
        return rightAnswer?.takeIf { it.isNotBlank() }?.let {
            Specification { root, _, builder ->
                builder.equal(root.get<String>("rightAnswer"), rightAnswer)
            }
        }
    }

    fun hasDifficultyLevel(difficultyLevel: DifficultyLevel?): Specification<Question>? {
        return difficultyLevel?.let {
            Specification { root, _, builder ->
                builder.equal(root.get<DifficultyLevel>("difficultyLevel"), difficultyLevel)
            }
        }
    }

    fun createSpecification(criteria: QuestionSearchCriteria): Specification<Question> {
        return Specification.allOf(
            hasSkill(criteria.skill),
            containsTitle(criteria.title),
            hasRightAnswer(criteria.rightAnswer),
            hasDifficultyLevel(criteria.difficultyLevel)
        )
    }
}