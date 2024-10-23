package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "idea")
open class Idea (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "title")
    open var title: String,

    @Column(name = "description")
    open var description: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var owner: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    open var status: Status,

    @Column(name = "creation_date")
    open var creationDate: Date,

    @ManyToMany
    @JoinTable(
        name = "idea_tags",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    open var tags: MutableList<Tag>?,

    @ManyToMany
    @JoinTable(
        name = "idea_juries",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var requiredJuries: MutableList<User>?,

    @OneToMany(mappedBy = "idea", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var comments: MutableList<Comment>?,

    @ManyToOne
    @JoinColumn(name = "idea_box_id")
    open var ideaBox: IdeaBox,

    @ManyToMany
    @JoinTable(
        name = "idea_likes",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var likes: MutableList<User>?,

    @OneToMany(mappedBy = "idea", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var scoreSheets: MutableList<ScoreSheet>

)