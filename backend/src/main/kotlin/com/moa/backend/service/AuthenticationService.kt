package com.moa.backend.service

import com.moa.backend.model.*
import com.moa.backend.repository.UserRepository
import com.moa.backend.security.auth.AuthenticationRequest
import com.moa.backend.security.auth.AuthenticationResponse
import com.moa.backend.security.auth.RegisterRequest
import com.moa.backend.security.config.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService {

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var jwtService: JwtService
    @Autowired
    lateinit var pwEncoder: PasswordEncoder
    @Autowired
    lateinit var authManager: AuthenticationManager

    fun register(request: RegisterRequest): AuthenticationResponse {
        val user = User(
            id = 0L,
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            password = pwEncoder.encode(request.password),
            role = Role.USER,
            likedIdeas = emptyList<Idea>().toMutableList(),
            likedComments = emptyList<Comment>().toMutableList(),
            ideas = emptyList<Idea>().toMutableList(),
            ideaBoxes = emptyList<IdeaBox>().toMutableList(),
            comments = emptyList<Comment>().toMutableList(),
        )
        userRepository.saveAndFlush(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(
            token = jwtToken
        )
    }

    fun authenticate(request: AuthenticationRequest): AuthenticationResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )
        val user = userRepository.findByEmail(request.email)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(
            token = jwtToken
        )
    }

}