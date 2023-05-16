package com.moa.backend.model

import javax.persistence.*

@Entity
data class Score (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var score: Int,

    @Enumerated(EnumType.STRING)
    var type: ScoreType,

    @ManyToOne
    @JoinColumn(name = "idea_id")
    var idea: Idea,

    var title: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User

)
