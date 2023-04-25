package com.moa.backend.security.auth

data class AuthenticationRequest (
    val email: String,

    val password: String,
)
