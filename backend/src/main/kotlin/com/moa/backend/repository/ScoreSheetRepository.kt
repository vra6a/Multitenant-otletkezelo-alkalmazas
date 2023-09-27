package com.moa.backend.repository

import com.moa.backend.model.ScoreSheet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ScoreSheetRepository : JpaRepository<ScoreSheet, Long>