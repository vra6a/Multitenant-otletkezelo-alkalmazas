package com.moa.backend.mapper

import com.moa.backend.model.Comment
import com.moa.backend.model.User
import com.moa.backend.model.dto.CommentDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentMapper: Mapper<CommentDto, CommentSlimDto, Comment> {

    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var commentRepository: CommentRepository


    override fun modelToDto(entity: Comment): CommentDto {
        TODO("Not yet implemented")
    }

    override fun modelToSlimDto(entity: Comment): CommentSlimDto {
        return CommentSlimDto(
            id = entity.id,
            owner = userMapper.modelToSlimDto(entity.owner),
            creationDate = entity.creationDate,
            text = entity.text
        )
    }

    override fun dtoToModel(domain: CommentDto): Comment {
        if(domain.id == 0L) {
            return Comment(
                    id = domain.id,
                    creationDate = domain.creationDate,
                    text = domain.text,
                    owner = userMapper.slimDtoToModel(domain.owner),
                    idea = ideaMapper.slimDtoToModel(domain.idea),
                    likes = emptyList<User>().toMutableList(),
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: CommentSlimDto): Comment {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): Comment {
        val comment = commentRepository.findById(id).orElse(null)

        return Comment(
            id = comment.id,
            creationDate = comment.creationDate,
            text = comment.text,
            owner = comment.owner,
            idea = comment.idea,
            likes = comment.likes
        )
    }
}