package com.moa.backend.model

import javax.persistence.*

@Entity
data class ScoreItem (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Enumerated(EnumType.STRING)
    var type: ScoreType,

    @ManyToOne
    @JoinColumn(name = "scoreSheet_id")
    var scoreSheet: ScoreSheet,

    var title: String,

    var score: Int?,

    var text: String?,
)
