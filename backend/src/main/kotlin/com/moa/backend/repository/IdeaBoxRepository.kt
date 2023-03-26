package com.moa.backend.repository

import com.moa.backend.model.IdeaBox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IdeaBoxRepository : JpaRepository<IdeaBox, Long>