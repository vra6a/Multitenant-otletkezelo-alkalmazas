package com.moa.backend.mapper

import com.moa.backend.model.Comment
import com.moa.backend.model.Idea
import com.moa.backend.model.User
import com.moa.backend.model.dto.CommentDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommentMapper: Mapper<CommentDto, CommentSlimDto, Comment> {

    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var commentRepository: CommentRepository


    override fun modelToDto(entity: Comment): CommentDto {

        val likes: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        entity.likes.forEach{ user: User ->
            likes.add(userMapper.modelToSlimDto(user))
        }

        return CommentDto(
            id = entity.id,
            text = entity.text,
            creationDate = entity.creationDate,
            owner = userMapper.modelToSlimDto(entity.owner),
            idea = ideaMapper.modelToSlimDto(entity.idea),
            likes = likes,
            isEdited = entity.isEdited
        )
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
            val currentTenant = TenantContext.getCurrentTenant().orEmpty()
            return Comment(
                    id = domain.id,
                    creationDate = Date(),
                    text = domain.text,
                    owner = userMapper.slimDtoToModel(domain.owner),
                    idea = ideaMapper.slimDtoToModel(domain.idea),
                    likes = emptyList<User>().toMutableList(),
                    isEdited = false,
                    tenantId = currentTenant
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
            likes = comment.likes,
            isEdited = comment.isEdited,
            tenantId = comment.tenantId
        )
    }
}