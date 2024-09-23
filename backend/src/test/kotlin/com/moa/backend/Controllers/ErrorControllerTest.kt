package com.moa.backend.Controllers

import com.moa.backend.controller.ErrorController
import com.moa.backend.utility.ErrorException
import io.jsonwebtoken.JwtException
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.NoHandlerFoundException
import javax.naming.AuthenticationException
import javax.validation.ConstraintViolationException

@SpringBootTest
@ExtendWith(MockKExtension::class)
class ErrorControllerTest {
    private lateinit var errorController: ErrorController

    @BeforeEach
    fun setUp() {
        errorController = ErrorController()
    }

    @Test
    fun `errorHandler should return NOT_ACCEPTABLE status when ErrorException is thrown`() {
        val errorException = ErrorException("Custom error message")

        val response = errorController.errorHandler(errorException)

        assertEquals(HttpStatus.NOT_ACCEPTABLE.value(), response.code)
        assertEquals("Custom error message", response.message)
    }

    @Test
    fun `notFoundHandler should return NOT_FOUND status when NoHandlerFoundException is thrown`() {
        val exception = NoHandlerFoundException("GET", "/some-path", HttpHeaders())

        val response = errorController.notFoundHandler(exception)

        assertEquals(HttpStatus.NOT_FOUND.value(), response.code)
        assertEquals("No handler found for GET /some-path", response.message)
    }

    @Test
    fun `validationHandler should return EXPECTATION_FAILED status when ConstraintViolationException is thrown`() {
        val constraintViolationException = ConstraintViolationException("Validation failed", null)

        val response = errorController.validationHandler(constraintViolationException)

        assertEquals(HttpStatus.EXPECTATION_FAILED.value(), response.code)
        assertEquals("Validation failed", response.message)
    }

    @Test
    fun `unAuthorizedHandler should return UNAUTHORIZED status when AuthenticationException is thrown`() {
        val authenticationException = object : AuthenticationException("Authentication failed") {}

        val response = errorController.unAuthorizedHandler(authenticationException)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.code)
        assertEquals("Authentication failed", response.message)
    }

    @Test
    fun `jwtExceptionHandler should return UNAUTHORIZED status when JwtException is thrown`() {
        val jwtException = JwtException("Invalid JWT token")

        val response = errorController.jwtExceptionHandler(jwtException)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.code)
        assertEquals("Invalid JWT token", response.message)
    }
}