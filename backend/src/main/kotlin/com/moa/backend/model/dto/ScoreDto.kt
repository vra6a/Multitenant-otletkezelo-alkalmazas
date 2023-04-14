package com.moa.backend.model.dto

import com.moa.backend.model.ScoreType
import com.moa.backend.model.slim.IdeaSlimDto

data class ScoreDto (

    var id: Long = 0,

    var score: Int,

    var type: ScoreType,

    var idea: IdeaSlimDto,

)