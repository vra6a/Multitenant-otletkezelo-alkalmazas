package com.moa.backend.model

import javax.persistence.*

@Entity
data class ScoreSheet(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @OneToMany(mappedBy = "scoreSheet", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var scores: MutableList<ScoreItem>?,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User,

    @ManyToOne
    @JoinColumn(name = "idea_id")
    var idea: Idea,

    @OneToOne(mappedBy = "scoreSheetTemplate")
    var templateFor: IdeaBox
)
