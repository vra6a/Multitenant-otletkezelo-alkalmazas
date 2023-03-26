package com.moa.backend.model

import java.util.Date
import javax.persistence.*

@Entity
data class Idea (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    val description: String,

    @OneToMany(mappedBy = "idea")
    val score: Set<Score>,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val owner: User,

    @Enumerated(EnumType.STRING)
    val status: Status,

    val creationDate: Date,

    @ManyToMany
    @JoinTable(
        name = "idea_tags",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: List<Tag>,

    @OneToMany(mappedBy = "idea")
    val comments: List<Comment>,

    @ManyToOne
    @JoinColumn(name = "ideaBox_id", nullable = false)
    val ideaBox: IdeaBox,

    @ManyToMany
    @JoinTable(
        name = "idea_likes",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    val likes: Set<User>

)