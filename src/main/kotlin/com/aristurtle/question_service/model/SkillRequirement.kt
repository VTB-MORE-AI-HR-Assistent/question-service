package com.aristurtle.question_service.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "skill_requirements")
data class SkillRequirement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    val position: Position,

    @ManyToOne
    @JoinColumn(name = "skill_id", nullable = false)
    val skill: Skill,

    @Column(name = "easy", nullable = false)
    val easy: Short,

    @Column(name = "medium", nullable = false)
    val medium: Short,

    @Column(name = "hard", nullable = false)
    val hard: Short
) {
    init {
        require(easy in 0..100) { "Easy must be between 0 and 100" }
        require(medium in 0..100) { "Medium must be between 0 and 100" }
        require(hard in 0..100) { "Hard must be between 0 and 100" }
        require(easy + medium + hard == 100) { "Sum of the easy, medium, hard must equals 100" }
    }
}
