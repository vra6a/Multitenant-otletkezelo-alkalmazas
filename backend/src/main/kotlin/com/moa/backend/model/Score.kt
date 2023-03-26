package com.moa.backend.model

import javax.persistence.*

@Entity
data class Score (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val score: Int,

    @Enumerated(EnumType.STRING)
    val type: ScoreType,

    @ManyToOne
    @JoinColumn(name = "idea_id", nullable = false)
    val idea: Idea,

)
