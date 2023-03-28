package com.moa.backend.service

import com.moa.backend.model.User
import com.moa.backend.repository.CommentRepository
import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentService {

    @Autowired
    lateinit var commentRepository: CommentRepository


}