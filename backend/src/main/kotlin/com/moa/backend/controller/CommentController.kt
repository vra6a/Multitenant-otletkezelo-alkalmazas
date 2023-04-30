package com.moa.backend.controller

import com.moa.backend.mapper.CommentMapper
import com.moa.backend.model.dto.CommentDto
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.repository.CommentRepository
import com.moa.backend.service.CommentService
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class CommentController {

    @Autowired
    lateinit var commentService: CommentService

    @GetMapping("/comments/{id}")
    fun getCommentsByIdea(@PathVariable id: Long): ResponseEntity<*> {
        return commentService.getCommentsByIdea(id)
    }

    @GetMapping("/comment/{id}")
    fun GetComment(@PathVariable id: Long): ResponseEntity<*> {
        return commentService.getComment(id)
    }

    @PostMapping("/comment")
    fun createComment(@RequestBody comment: CommentDto): ResponseEntity<*> {
        return  commentService.createComment(comment)
    }

    @PostMapping("/comment/{id}/like")
    fun likeComment(@PathVariable id: Long): ResponseEntity<*> {
        return commentService.likeComment(id)
    }

    @PostMapping("/comment/{id}/dislike")
    fun dislikeComment(@PathVariable id: Long): ResponseEntity<*> {
        return commentService.dislikeComment(id)
    }

}