package com.moa.backend.model

import java.util.*
import javax.persistence.*

@Entity
data class Comment (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val creationDate: Date,

    val text: String,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val owner: User,

    @ManyToOne
    @JoinColumn(name = "idea_id", nullable = false)
    val idea: Idea,

    @ManyToMany
    @JoinTable(
        name = "comment_likes",
        joinColumns = [JoinColumn(name = "comment_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val likes: Set<User>
)
