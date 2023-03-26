package com.moa.backend.controller

import com.moa.backend.repository.CommentRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/comment")
class CommentController(private val commentRepository: CommentRepository) {
    //TODO
}