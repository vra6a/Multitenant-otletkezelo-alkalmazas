package com.moa.backend.utility

import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.IdeaSlimDto
import org.springframework.http.HttpStatus

data class IdeaScoreSheets (
    val idea: IdeaSlimDto,
    val scoreSheet: MutableList<ScoreSheetDto>
)