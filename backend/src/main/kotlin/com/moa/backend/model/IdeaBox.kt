package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "ideabox")
open class IdeaBox (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "name")
    open var name: String,

    @Column(name = "description")
    open var description: String,

    @Column(name = "start_date")
    open var startDate: Date,

    @Column(name = "end_date")
    open var endDate: Date,

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var creator: User,

    @OneToMany(mappedBy = "ideaBox", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var ideas: MutableList<Idea>,

    @ManyToMany
    @JoinTable(
        name = "required_juries",
        joinColumns = [JoinColumn(name = "idea_box_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var defaultRequiredJuries: MutableList<User>?,

    @OneToMany(mappedBy = "templateFor", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var scoreSheetTemplates: MutableList<ScoreSheet>,

    @Column(name = "is_sclosed")
    open var isSclosed: Boolean,
) {
    fun copy(
        id: Long = this.id,
        name: String = this.name,
        description: String = this.description,
        startDate: Date = this.startDate,
        endDate: Date = this.endDate,
        creator: User = this.creator,
        ideas: MutableList<Idea> = this.ideas,
        defaultRequiredJuries: MutableList<User>? = this.defaultRequiredJuries,
        scoreSheetTemplates: MutableList<ScoreSheet> = this.scoreSheetTemplates,
        isSclosed: Boolean = this.isSclosed
    ): IdeaBox {
        return IdeaBox(
            id = id,
            name = name,
            description = description,
            startDate = startDate,
            endDate = endDate,
            creator = creator,
            ideas = ideas,
            defaultRequiredJuries = defaultRequiredJuries,
            scoreSheetTemplates = scoreSheetTemplates,
            isSclosed = isSclosed
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IdeaBox

        if (id != other.id) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false
        if (creator != other.creator) return false
        if (ideas != other.ideas) return false
        if (defaultRequiredJuries != other.defaultRequiredJuries) return false
        if (scoreSheetTemplates != other.scoreSheetTemplates) return false
        if (isSclosed != other.isSclosed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + startDate.hashCode()
        result = 31 * result + endDate.hashCode()
        result = 31 * result + creator.hashCode()
        result = 31 * result + ideas.hashCode()
        result = 31 * result + (defaultRequiredJuries?.hashCode() ?: 0)
        result = 31 * result + scoreSheetTemplates.hashCode()
        result = 31 * result + isSclosed.hashCode()
        return result
    }
}