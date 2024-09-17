package com.moa.backend.model.slim

import com.moa.backend.model.Judgement
import com.moa.backend.model.Status

data class IdeaSlimDto (

    var id: Long = 0,

    var title: String,

    var status: Status,

    var judgement: Judgement,
)