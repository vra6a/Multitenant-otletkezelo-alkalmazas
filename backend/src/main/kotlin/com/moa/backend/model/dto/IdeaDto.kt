package com.moa.backend.model.dto

import com.moa.backend.model.*
import com.moa.backend.model.slim.*
import java.util.*

data class IdeaDto (

    var id: Long = 0,

    var title: String,

    var description: String,

    var owner: UserSlimDto,

    var status: Status,

    var creationDate: Date?,

    var tags: MutableList<TagSlimDto>?,

    var comments: MutableList<CommentSlimDto>?,

    var ideaBox: IdeaBoxSlimDto,

    var likes: MutableList<UserSlimDto>?,

    var requiredJuries: MutableList<UserSlimDto>?,

    var scoreSheets: MutableList<ScoreSheetSlimDto>?,
)