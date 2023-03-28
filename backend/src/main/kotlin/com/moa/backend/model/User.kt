package com.moa.backend.model

import javax.persistence.*

@Entity
data class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var firstName: String,

    var lastName: String,

    var email: String,

    @Enumerated(EnumType.STRING)
    var role: Role,

    @ManyToMany(mappedBy = "likes")
    var likedIdeas: Set<Idea>?,

    @ManyToMany(mappedBy = "likes")
    var likedComments: Set<Comment>?,

    @OneToMany(mappedBy = "owner")
    var ideas: Set<Idea>?,

    @OneToMany(mappedBy = "creator")
    var ideaBoxes: Set<IdeaBox>?,

    @OneToMany(mappedBy = "owner")
    var comments: Set<Comment>?
)