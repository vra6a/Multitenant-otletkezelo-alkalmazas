package com.moa.backend.model

import javax.persistence.*

@Entity
open class Tag (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0,

    open var name: String,

    @ManyToMany(mappedBy = "tags")
    open var taggedIdeas: MutableList<Idea>?


)