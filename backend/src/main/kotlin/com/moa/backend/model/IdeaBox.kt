package com.moa.backend.model

import java.util.Date
import javax.persistence.*

@Entity
data class IdeaBox (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String,

    val description: String,

    val startDate: Date,

    val endDate: Date,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val creator: User,

    @OneToMany(mappedBy = "ideaBox")
    val ideas: Set<Idea>
)