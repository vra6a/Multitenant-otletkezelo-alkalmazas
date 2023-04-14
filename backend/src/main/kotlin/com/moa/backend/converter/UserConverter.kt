package com.moa.backend.converter

import com.moa.backend.model.Comment
import com.moa.backend.model.Idea
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import org.mapstruct.InheritInverseConfiguration

import org.mapstruct.factory.Mappers

class UserConverter() {

    private val userConverter = UserConverter()
    private val commentConverter = CommentConverter()
    private val ideaBoxConverter = IdeaBoxConverter()
    private val ideaConverter = IdeaConverter()

    @InheritInverseConfiguration
    fun convertToDto(user: User): UserDto {

        val likedComments: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()
        user.likedComments?.forEach{ comment: Comment ->
            likedComments.add(commentConverter.convertToSlimDto(comment))
        }

        val comments: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()
        user.comments?.forEach{ comment: Comment ->
            comments.add(commentConverter.convertToSlimDto(comment))
        }

        val ideaBoxes: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()
        user.ideaBoxes?.forEach{ ideaBox: IdeaBox ->
            ideaBoxes.add(ideaBoxConverter.convertToSlimDto(ideaBox))
        }

        val ideas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        user.ideas?.forEach{ idea: Idea ->
            ideas.add(ideaConverter.convertToSlimDto(idea))
        }

        val likedIdeas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        user.likedIdeas?.forEach{ idea: Idea ->
            likedIdeas.add(ideaConverter.convertToSlimDto(idea))
        }

        return UserDto(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            role = user.role,
            likedComments = likedComments,
            comments = comments,
            ideaBoxes = ideaBoxes,
            ideas = ideas,
            likedIdeas = likedIdeas
        )
    }

    fun convertToSlimDto(user: User): UserSlimDto {
        return UserSlimDto(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
    /*
    fun convertToModel(user: UserDto): User {

    }
     */

}