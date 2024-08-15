package com.moa.backend.model.dto.utility

import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto

data class BulkIdeaDto (

    var ideaBox: IdeaBoxSlimDto,

    var ideas: MutableList<IdeaSlimDto>
)