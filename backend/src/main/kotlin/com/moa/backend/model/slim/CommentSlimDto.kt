package com.moa.backend.model.slim

import java.util.*

data class CommentSlimDto (
    var id: Long = 0,

    var creationDate: Date,

    var text: String,

    var owner: UserSlimDto,
)