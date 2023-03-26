package com.moa.backend.model

import javax.persistence.*

@Entity
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val firstName: String,

    val lastName: String,

    val email: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @ManyToMany(mappedBy = "likes")
    val likedIdeas: Set<Idea>,

    @ManyToMany(mappedBy = "likes")
    val likedComments: Set<Comment>,

    @OneToMany(mappedBy = "owner")
    val ideas: Set<Idea>,

    @OneToMany(mappedBy = "creator")
    val ideaBoxes: Set<IdeaBox>,

    @OneToMany(mappedBy = "owner")
    val comments: Set<Comment>
)