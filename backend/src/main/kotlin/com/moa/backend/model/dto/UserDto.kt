package com.moa.backend.model.dto

import com.moa.backend.model.Role
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto

data class UserDto(
     var id: Long,

     var firstName: String,

     var lastName: String,

     var email: String,

     var password: String,

     var role: Role,

     var likedIdeas: MutableList<IdeaSlimDto>?,

     var likedComments: MutableList<CommentSlimDto>?,

     var ideas: MutableList<IdeaSlimDto>?,

     var ideaBoxes: MutableList<IdeaBoxSlimDto>?,

     var comments: MutableList<CommentSlimDto>?,
)