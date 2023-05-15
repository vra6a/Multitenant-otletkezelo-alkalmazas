package com.moa.backend.repository

import com.moa.backend.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    @Query("SELECT u FROM User u WHERE u.role='Jury' OR u.role='ADMIN'")
    fun findJuries(): List<User>
}