package com.moa.backend.model

import javax.persistence.*

@Entity
@Table(name = "score_sheet")
open class ScoreSheet(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @OneToMany(mappedBy = "scoreSheet", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var scores: MutableList<ScoreItem>?,

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var owner: User,

    @ManyToOne
    @JoinColumn(name = "idea_id")
    open var idea: Idea?,

    @ManyToOne
    @JoinColumn(name = "idea_box_id")
    open var templateFor: IdeaBox?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScoreSheet) return false

        return id == other.id &&
                owner == other.owner &&
                idea == other.idea &&
                templateFor == other.templateFor

    }
}
