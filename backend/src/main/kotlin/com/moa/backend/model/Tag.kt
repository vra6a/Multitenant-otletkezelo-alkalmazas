package com.moa.backend.model

import javax.persistence.*

@Entity
data class Tag (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    @ManyToMany(mappedBy = "tags")
    val taggedIdeas: Set<Idea>


)