package com.moa.backend.model

import java.util.Date
import javax.persistence.*

@Entity
data class IdeaBox (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var name: String,

    var description: String,

    var startDate: Date,

    var endDate: Date,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var creator: User,

    @OneToMany(mappedBy = "ideaBox")
    var ideas: Set<Idea>
) {
    public fun IdeaBox.toIdeaBoxListView() = IdeaBoxListView(
        id = id,
        name = name,
        endDate = endDate
    )
}