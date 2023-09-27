package com.moa.backend.model.slim

data class ScoreSheetSlimDto(
    var id: Long = 0,

    var owner: UserSlimDto,

    var idea: IdeaSlimDto
)
