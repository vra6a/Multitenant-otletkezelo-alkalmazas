package com.moa.backend.model.dto

import com.moa.backend.model.ScoreType
import com.moa.backend.model.User
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto

data class ScoreDto (

    var id: Long = 0,

    var title: String,

    var score: Int,

    var type: ScoreType,

    var idea: IdeaSlimDto,

    var owner: UserSlimDto,

    )