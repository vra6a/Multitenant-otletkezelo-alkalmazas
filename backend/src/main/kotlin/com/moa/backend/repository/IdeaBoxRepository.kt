package com.moa.backend.repository

import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface IdeaBoxRepository : JpaRepository<IdeaBox, Long> {

    @Query("SELECT i FROM IdeaBox i WHERE i.name LIKE %?1%")
    fun search(s: String, pageable: Pageable): List<IdeaBox>

    @Query("SELECT DISTINCT ib FROM IdeaBox ib JOIN ib.ideas i WHERE i.scoreSheets IS NOT EMPTY")
    fun findIdeaBoxesWithIdeasHavingScoreSheets(): List<IdeaBox>

    @Query("SELECT COUNT(i) FROM Idea i WHERE i.ideaBox.id = :ideaBoxId AND i.scoreSheets IS NOT EMPTY")
    fun countScoredIdeasByIdeaBoxId(@Param("ideaBoxId") ideaBoxId: Long): Long

    @Query("SELECT ib.defaultRequiredJuries FROM IdeaBox ib WHERE ib.id = :ideaBoxId")
    fun findRequiredJuriesByIdeaBoxId(@Param("ideaBoxId") ideaBoxId: Long): List<User>
}