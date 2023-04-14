package com.moa.backend.model.dto

import com.moa.backend.model.slim.IdeaSlimDto

data class TagDto (

    var id: Long = 0,

    var name: String,

    var taggedIdeas: MutableList<IdeaSlimDto>,
)