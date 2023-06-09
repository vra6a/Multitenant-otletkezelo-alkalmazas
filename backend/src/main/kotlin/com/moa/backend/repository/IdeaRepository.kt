package com.moa.backend.repository

import com.moa.backend.model.Idea
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IdeaRepository : JpaRepository<Idea, Long> {

    @Query("SELECT i FROM Idea i WHERE i.status='REVIEWED'")
    fun getReviewedIdeas(): List<Idea>
}