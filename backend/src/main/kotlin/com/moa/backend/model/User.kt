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
    var likedIdeas: MutableList<Idea>?,

    @ManyToMany(mappedBy = "likes")
    var likedComments: MutableList<Comment>?,

    @OneToMany(mappedBy = "owner")
    var ideas: MutableList<Idea>?,

    @OneToMany(mappedBy = "creator")
    var ideaBoxes: MutableList<IdeaBox>?,

    @OneToMany(mappedBy = "owner")
    var comments: MutableList<Comment>?
)