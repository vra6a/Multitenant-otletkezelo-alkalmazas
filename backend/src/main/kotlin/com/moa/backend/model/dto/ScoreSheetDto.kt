package com.moa.backend.model.dto

import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.UserSlimDto

data class ScoreSheetDto(

    var id: Long = 0,

    var scores: MutableList<ScoreItemSlimDto>,

    var owner: UserSlimDto,

    var idea: IdeaSlimDto,

    var templateFor: IdeaBoxSlimDto
)
