package com.moa.backend.model

import javax.persistence.*

@Entity
data class Tag (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    @ManyToMany(mappedBy = "tags")
    var taggedIdeas: MutableList<Idea>?


)