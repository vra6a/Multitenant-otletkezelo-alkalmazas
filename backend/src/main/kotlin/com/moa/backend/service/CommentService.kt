package com.moa.backend.service

import com.moa.backend.mapper.CommentMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Comment
import com.moa.backend.model.User
import com.moa.backend.model.dto.CommentDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.repository.CommentRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    lateinit var commentRepository: CommentRepository

    @Autowired
    lateinit var commentMapper: CommentMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userMapper: UserMapper

    fun getComment(id: Long): ResponseEntity<*> {
        val comment = commentRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<CommentDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = commentMapper.modelToDto(comment)
            )
        )
    }

    fun getCommentSlim(id: Long): ResponseEntity<*> {
        val comment = commentRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<CommentSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = commentMapper.modelToSlimDto(comment)
            )
        )
    }

    fun getCommentsByIdea(id: Long): ResponseEntity<*> {
        val comments = commentRepository.getCommentsByIdeaId(id)
        val response: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()

        for (comment in comments) {
            comment.let {
                response.add((commentMapper.modelToSlimDto(comment)))
            }
        }
        return ResponseEntity.ok(
            WebResponse<MutableList<CommentSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createComment(comment: CommentDto): ResponseEntity<*> {
        return ResponseEntity.ok(
            WebResponse<CommentDto>(
                code = HttpStatus.OK.value(),
                message = "Comment successfully created!",
                data = commentMapper.modelToDto(
                    commentRepository.saveAndFlush(
                        commentMapper.dtoToModel(comment)
                    )
                )
            )
        )
    }

    fun likeComment(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null) ?:
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        val comment = commentRepository.findById(id).orElse(null) ?:
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        comment.likes.add(user)
        commentRepository.saveAndFlush(comment)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Comment Liked!",
                data = "Comment Liked!"
            )
        )
    }

    fun dislikeComment(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null) ?:
        return ResponseEntity(
            WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                message = "Authentication error!",
                data = null
            ),
            HttpStatus.NOT_FOUND
        )
        val comment = commentRepository.findById(id).orElse(null) ?:
        return ResponseEntity(
            WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                message = "Cannot find Comment with this id $id!",
                data = null
            ),
            HttpStatus.NOT_FOUND
        )

        comment.likes.remove(user)
        commentRepository.saveAndFlush(comment)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Comment Disliked!",
                data = "Comment Disliked!"
            )
        )
    }

}