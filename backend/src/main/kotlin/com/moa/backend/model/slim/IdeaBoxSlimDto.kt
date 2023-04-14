package com.moa.backend.model.slim

import java.util.*

data class IdeaBoxSlimDto (

    var id: Long = 0,

    var name: String,

    var startDate: Date,

    var endDate: Date,
)