package com.moa.backend.model


import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import java.util.*
import javax.persistence.*


@Entity
@FilterDef(name = "tenantFilter", parameters = [ParamDef(name = "tenantId", type = "string")])
@Filters(
    Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
)
data class Comment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "tenant_id", nullable = false)
    var tenantId: String,

    var creationDate: Date,

    var text: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User,

    @ManyToOne
    @JoinColumn(name = "idea_id")
    var idea: Idea,

    var isEdited: Boolean,

    @ManyToMany
    @JoinTable(
        name = "comment_likes",
        joinColumns = [JoinColumn(name = "comment_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var likes: MutableList<User>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Comment) return false

        return id == other.id &&
                creationDate == other.creationDate &&
                text == other.text &&
                isEdited == other.isEdited
    }
}
