package com.moa.backend.converter

import com.moa.backend.model.Idea
import com.moa.backend.model.slim.IdeaSlimDto
import org.mapstruct.Mapper

@Mapper
class IdeaConverter {

    fun convertToSlimDto(idea: Idea): IdeaSlimDto {
        return IdeaSlimDto(
            id = idea.id,
            status = idea.status,
            title = idea.title
        )
    }
}