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
import mu.KotlinLogging
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

    private val logger = KotlinLogging.logger {}

    fun getComment(id: Long): ResponseEntity<*> {
        val comment = commentRepository.findById(id).orElse(null)
        if(comment == null) {
            logger.info { "MOA-INFO: Comment with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: Comment with id: ${id} found." }
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
        if(comment == null) {
            logger.info { "MOA-INFO: Comment with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: Comment with id: ${id} found." }

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

        logger.info { "MOA-INFO: Comments found." }

        return ResponseEntity.ok(
            WebResponse<MutableList<CommentSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createComment(comment: CommentDto): ResponseEntity<*> {
        val data = commentMapper.modelToDto(commentRepository.save(commentMapper.dtoToModel(comment)))
        logger.info { "MOA-INFO: Comment created with id: ${data.id}. Comment: $data" }
        return ResponseEntity.ok(
            WebResponse<CommentDto>(
                code = HttpStatus.OK.value(),
                message = "Comment successfully created!",
                data = data
                )
        )
    }

    fun editComment(comment: CommentSlimDto): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        if(comment.owner.email != authentication.name) {
            logger.info { "MOA-INFO: Comment edit with id: ${comment.id} failed. Reason: Editing user is not the creator user" }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "You dont have permission to do that!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val originalComment = commentRepository.findById(comment.id).orElse(null)
        if(originalComment == null) {
            logger.info { "MOA-INFO: Comment with id: ${comment.id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $comment.id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        originalComment.isEdited = true
        originalComment.text = comment.text

        val data = commentMapper.modelToDto(commentRepository.save(originalComment))
        logger.info { "MOA-INFO: Comment edited with id: ${data.id}. Comment: $data" }
        return ResponseEntity.ok(
            WebResponse<CommentDto>(
                code = HttpStatus.OK.value(),
                message = "Comment successfully edited!",
                data = data
            )
        )
    }

    fun likeComment(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)
        if(user == null) {
            logger.info { "MOA-INFO: Authentication error during comment editing. Comment id: ${id}." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val comment = commentRepository.findById(id).orElse(null)
        if(comment == null) {
            logger.info { "MOA-INFO: Comment with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        comment.likes.add(user)
        commentRepository.save(comment)
        logger.info { "MOA-INFO: Comment with id: ${comment.id} liked by user ${user.email}." }

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
        val user = userRepository.findByEmail(authentication.name).orElse(null)
        if(user == null) {
            logger.info { "MOA-INFO: Authentication error during comment editing. Comment id: ${id}." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val comment = commentRepository.findById(id).orElse(null)
        if(comment == null) {
            logger.info { "MOA-INFO: Comment with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Comment with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        comment.likes.remove(user)
        commentRepository.save(comment)
        logger.info { "MOA-INFO: Comment with id: ${comment.id} disliked by user ${user.email}." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Comment Disliked!",
                data = "Comment Disliked!"
            )
        )
    }
}