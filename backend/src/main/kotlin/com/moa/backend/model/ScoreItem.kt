package com.moa.backend.model

import javax.persistence.*

@Entity
@Table(name = "score_item")
open class ScoreItem (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    open var type: ScoreType,

    @ManyToOne
    @JoinColumn(name = "score_sheet_id")
    open var scoreSheet: ScoreSheet,

    @Column(name = "title")
    open var title: String,

    @Column(name = "score")
    open var score: Int?,

    @Column(name = "text")
    open var text: String?,
)
