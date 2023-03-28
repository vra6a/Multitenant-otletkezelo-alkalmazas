package com.moa.backend.model

import java.util.Date
import javax.persistence.*

@Entity
data class Idea (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String,

    var description: String,

    @OneToMany(mappedBy = "idea")
    var score: Set<Score>,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var owner: User,

    @Enumerated(EnumType.STRING)
    var status: Status,

    var creationDate: Date,

    @ManyToMany
    @JoinTable(
        name = "idea_tags",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: List<Tag>?,

    @OneToMany(mappedBy = "idea")
    var comments: List<Comment>?,

    @ManyToOne
    @JoinColumn(name = "ideaBox_id")
    var ideaBox: IdeaBox,

    @ManyToMany
    @JoinTable(
        name = "idea_likes",
        joinColumns = [JoinColumn(name = "idea_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")]
    )
    var likes: Set<User>?

)