package com.aristurtle.question_service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "questions")
data class Question(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "skill_id", referencedColumnName = "id")
    val skill: Skill,

    val title: String,

    val rightAnswer: String?,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    val difficultyLevel: DifficultyLevel
)
