package com.moa.backend.model.slim

import com.moa.backend.model.ScoreType

data class ScoreSlimDto (

    var id: Long = 0,

    var score: Int,

    var type: ScoreType,

    var title: String,

)