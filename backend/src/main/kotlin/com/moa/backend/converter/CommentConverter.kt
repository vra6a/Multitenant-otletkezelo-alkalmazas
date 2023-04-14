package com.moa.backend.converter

import com.moa.backend.model.Comment
import com.moa.backend.model.slim.CommentSlimDto
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

class CommentConverter {

    private val userConverter = UserConverter()

    fun convertToSlimDto(comment: Comment): CommentSlimDto {
        return CommentSlimDto(
            id = comment.id,
            owner = userConverter.convertToSlimDto(comment.owner),
            creationDate = comment.creationDate,
            text = comment.text
        )
    }
}