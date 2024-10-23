package com.moa.backend.model


import java.util.*
import javax.persistence.*


@Entity
@Table(name = "comment")
open class Comment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    open var id: Long = 0,

    @Column(name = "creation_date")
    open var creationDate: Date,

    @Column(name = "text")
    open var text: String,

    @ManyToOne
    @JoinColumn(name = "user_id")
    open var owner: User,

    @ManyToOne
    @JoinColumn(name = "idea_id")
    open var idea: Idea,

    @Column(name = "is_edited")
    open var isEdited: Boolean,

    @ManyToMany
    @JoinTable(
        name = "comment_likes",
        joinColumns = [JoinColumn(name = "comment_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    open var likes: MutableList<User>
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
