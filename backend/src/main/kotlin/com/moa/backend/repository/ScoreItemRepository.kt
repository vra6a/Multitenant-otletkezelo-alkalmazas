package com.moa.backend.repository

import com.moa.backend.model.ScoreItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreItemRepository : JpaRepository<ScoreItem, Long>