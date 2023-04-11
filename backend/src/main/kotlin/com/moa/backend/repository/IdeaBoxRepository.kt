package com.moa.backend.repository

import com.moa.backend.model.IdeaBox
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface IdeaBoxRepository : JpaRepository<IdeaBox, Long> {

    @Query("select i from IdeaBox i where i.name like %?1%")
    fun search(s: String, pageable: Pageable): List<IdeaBox>
}