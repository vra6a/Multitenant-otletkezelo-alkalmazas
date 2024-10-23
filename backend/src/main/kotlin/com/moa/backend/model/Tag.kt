package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
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