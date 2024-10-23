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
)