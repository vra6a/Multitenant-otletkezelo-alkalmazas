package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
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
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScoreItem

        if (id != other.id) return false
        if (type != other.type) return false
        if (scoreSheet != other.scoreSheet) return false
        if (title != other.title) return false
        if (score != other.score) return false
        if (text != other.text) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + scoreSheet.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (score?.hashCode() ?: 0)
        result = 31 * result + (text?.hashCode() ?: 0)
        return result
    }
}
