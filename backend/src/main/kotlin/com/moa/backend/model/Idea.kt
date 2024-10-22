package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import java.util.Date
import javax.persistence.*

@Entity
@FilterDef(name = "tenantFilter", parameters = [ParamDef(name = "tenantId", type = "string")])
@Filters(
    Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
)
data class Idea (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "tenant_id", nullable = false)
    var tenantId: String,

    var title: String,

    var description: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User,

    @Enumerated(EnumType.STRING)
    var status: Status,

    var creationDate: Date,

    @ManyToMany
    @JoinTable(
        name = "idea_tags",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: MutableList<Tag>?,

    @ManyToMany
    @JoinTable(
        name = "idea_juries",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var requiredJuries: MutableList<User>?,

    @OneToMany(mappedBy = "idea", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: MutableList<Comment>?,

    @ManyToOne
    @JoinColumn(name = "ideaBox_id")
    var ideaBox: IdeaBox,

    @ManyToMany
    @JoinTable(
        name = "idea_likes",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var likes: MutableList<User>?,

    @OneToMany(mappedBy = "idea", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var scoreSheets: MutableList<ScoreSheet>

)