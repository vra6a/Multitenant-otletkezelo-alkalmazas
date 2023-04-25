package com.moa.backend.security.auth

import com.moa.backend.model.Role
import org.mapstruct.Builder


data class AuthenticationResponse (
    val token: String = "",
    val id: Long = 0L,
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: Role,
)
