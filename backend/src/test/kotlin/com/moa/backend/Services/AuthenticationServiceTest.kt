package com.moa.backend.Services

import TestUtility
import com.moa.backend.model.*
import com.moa.backend.repository.UserRepository
import com.moa.backend.security.auth.AuthenticationRequest
import com.moa.backend.security.auth.RegisterRequest
import com.moa.backend.security.config.JwtService
import com.moa.backend.service.AuthenticationService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class AuthenticationServiceTest {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var jwtService: JwtService

    @MockkBean
    lateinit var pwEncoder: PasswordEncoder

    @MockkBean
    lateinit var authManager: AuthenticationManager

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `register() should successfully register a user`() {
        val registerRequest = RegisterRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "password123"
        )

        val user = User(
            id = 0L,
            firstName = registerRequest.firstName,
            lastName = registerRequest.lastName,
            email = registerRequest.email,
            password = "encodedPassword",
            role = Role.USER,
            likedIdeas = emptyList<Idea>().toMutableList(),
            likedComments = emptyList<Comment>().toMutableList(),
            ideas = emptyList<Idea>().toMutableList(),
            ideaBoxes = emptyList<IdeaBox>().toMutableList(),
            comments = emptyList<Comment>().toMutableList(),
            ideasToJury = emptyList<Idea>().toMutableList(),
            requiredToJury = emptyList<IdeaBox>().toMutableList(),
            scoreSheets = emptyList<ScoreSheet>().toMutableList(),
        )

        every { pwEncoder.encode(registerRequest.password) } returns "encodedPassword"
        every { userRepository.saveAndFlush(any<User>()) } returns user
        every { jwtService.generateToken(any<User>()) } returns "mockJwtToken"

        val response = authenticationService.register(registerRequest)

        assertEquals("mockJwtToken", response.token)
        assertEquals(user.id, response.id)
        assertEquals(user.firstName, response.firstName)
        assertEquals(user.lastName, response.lastName)
        assertEquals(user.email, response.email)
        assertEquals(user.role, response.role)

        verify { pwEncoder.encode(registerRequest.password) }
        verify { userRepository.saveAndFlush(any<User>()) }
    }

    @Test
    fun `authenticate() should successfully authenticate a user`() {
        val authRequest = AuthenticationRequest(
            email = "john.doe@example.com",
            password = "password123"
        )

        val user = User(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            email = authRequest.email,
            password = "encodedPassword",
            role = Role.USER,
            likedIdeas = emptyList<Idea>().toMutableList(),
            likedComments = emptyList<Comment>().toMutableList(),
            ideas = emptyList<Idea>().toMutableList(),
            ideaBoxes = emptyList<IdeaBox>().toMutableList(),
            comments = emptyList<Comment>().toMutableList(),
            ideasToJury = emptyList<Idea>().toMutableList(),
            requiredToJury = emptyList<IdeaBox>().toMutableList(),
            scoreSheets = emptyList<ScoreSheet>().toMutableList(),
        )

        every { authManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns mockk()
        every { userRepository.findByEmail(authRequest.email) } returns Optional.of(user)
        every { jwtService.generateToken(user) } returns "mockJwtToken"

        val response = authenticationService.authenticate(authRequest)

        assertEquals("mockJwtToken", response.token)
        assertEquals(user.id, response.id)
        assertEquals(user.firstName, response.firstName)
        assertEquals(user.lastName, response.lastName)
        assertEquals(user.email, response.email)
        assertEquals(user.role, response.role)

        verify { authManager.authenticate(any<UsernamePasswordAuthenticationToken>()) }
        verify { jwtService.generateToken(user) }
    }
}