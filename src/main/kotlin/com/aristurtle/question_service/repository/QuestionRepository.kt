package com.aristurtle.question_service.repository

import com.aristurtle.question_service.model.Question
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface QuestionRepository :
    JpaRepository<Question, Long>,
    PagingAndSortingRepository<Question, Long>,
    JpaSpecificationExecutor<Question> {
    @Query("SELECT COUNT(q) FROM Question q WHERE q.skill.id = :skillId")
    fun countBySkillId(@Param("skillId") skillId: Long): Long

    @Query("SELECT q FROM Question q WHERE q.skill.id = :skillId")
    fun findBySkillId(@Param("skillId") skillId: Long): List<Question>
}