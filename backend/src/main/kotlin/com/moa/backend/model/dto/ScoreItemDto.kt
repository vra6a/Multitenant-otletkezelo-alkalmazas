package com.moa.backend.model.dto

import com.moa.backend.model.ScoreType
import com.moa.backend.model.slim.ScoreSheetSlimDto

data class ScoreItemDto(

    var id: Long = 0,

    var type: ScoreType,

    var scoreSheet: ScoreSheetSlimDto,

    var title: String,

    var score: Int?,

    var text: String?,
)
