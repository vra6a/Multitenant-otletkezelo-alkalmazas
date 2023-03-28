package com.moa.backend.repository

import com.moa.backend.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository



@Repository
interface CommentRepository : JpaRepository<Comment, Long>