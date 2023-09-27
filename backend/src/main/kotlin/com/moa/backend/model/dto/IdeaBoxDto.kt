package com.moa.backend.model.dto

import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import java.util.*

data class IdeaBoxDto (

    var id: Long = 0,

    var name: String,

    var description: String,

    var startDate: Date,

    var endDate: Date,

    var creator: UserSlimDto,

    var ideas: MutableList<IdeaSlimDto>?,

    var defaultRequiredJuries: MutableList<UserSlimDto>?,

    var scoreSheetTemplate: ScoreSheet,
)