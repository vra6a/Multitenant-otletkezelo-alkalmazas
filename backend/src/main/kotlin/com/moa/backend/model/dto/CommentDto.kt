package com.moa.backend.model.dto

import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import java.util.*

data class CommentDto (

    var id: Long = 0,

    var creationDate: Date,

    var text: String,

    var owner: UserSlimDto,

    var idea: IdeaSlimDto,

    var likes: MutableList<UserSlimDto>?,
)