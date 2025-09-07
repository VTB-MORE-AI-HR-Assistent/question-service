package com.aristurtle.question_service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "positions")
data class Position(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "title", nullable = false, unique = true, length = 100)
    val title: String,

    @JsonIgnore
    @OneToMany(mappedBy = "position", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val skillRequirements: List<SkillRequirement> = emptyList()
)