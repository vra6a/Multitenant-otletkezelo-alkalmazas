package com.moa.backend.converter

import com.moa.backend.model.IdeaBox
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.UserSlimDto
import org.apache.catalina.User
import org.mapstruct.Mapper

class IdeaBoxConverter {

    //fun convertToDto(ideaBox: IdeaBox): IdeaBoxDto {}

    fun convertToSlimDto(ideaBox: IdeaBox): IdeaBoxSlimDto {
        return IdeaBoxSlimDto(
            id = ideaBox.id,
            name = ideaBox.name,
            startDate = ideaBox.startDate,
            endDate = ideaBox.endDate
        )
    }

    //fun convertToModel(ideaBox: IdeaBoxDto): IdeaBox
}