package com.moa.backend.model.slim

import com.moa.backend.model.ScoreType

data class ScoreItemSlimDto(

    var id: Long = 0,

    var type: ScoreType,

    var scoreSheet: ScoreSheetSlimDto,

    var title: String,

    var score: Int?,

    var text: String?,
)
