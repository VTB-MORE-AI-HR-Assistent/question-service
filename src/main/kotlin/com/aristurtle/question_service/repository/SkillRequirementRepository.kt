package com.aristurtle.question_service.repository

import com.aristurtle.question_service.model.SkillRequirement
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface SkillRequirementRepository : JpaRepository<SkillRequirement, Long> {
    fun findByPositionId(positionId: Long): List<SkillRequirement>
    fun findBySkillId(skillId: Long): List<SkillRequirement>
    fun findByPositionIdAndSkillId(positionId: Long, skillId: Long): Optional<SkillRequirement>

    @Query("SELECT sr FROM SkillRequirement sr WHERE sr.position.id = :positionId AND sr.skill.id IN :skillIds")
    fun findByPositionIdAndSkillIds(
        @Param("positionId") positionId: Long,
        @Param("skillIds") skillIds: List<Long>
    ): List<SkillRequirement>

    fun existsByPositionIdAndSkillId(positionId: Long, skillId: Long): Boolean
    fun deleteByPositionId(positionId: Long)
    fun deleteBySkillId(skillId: Long)
}