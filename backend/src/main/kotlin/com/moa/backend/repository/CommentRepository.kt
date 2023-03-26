package com.moa.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.xml.stream.events.Comment

@Repository
interface CommentRepository : JpaRepository<Comment, Long>