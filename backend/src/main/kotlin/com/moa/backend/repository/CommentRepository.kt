package com.moa.backend.repository

import com.moa.backend.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository



@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    @Query("select c from Comment c join c.idea i where i.id = :id")
    fun getCommentsByIdeaId(id: Long): List<Comment>
}