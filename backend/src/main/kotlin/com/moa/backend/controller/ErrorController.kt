package com.moa.backend.controller

import com.moa.backend.utility.ErrorException
import com.moa.backend.utility.WebResponse
import io.jsonwebtoken.JwtException
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ErrorController {
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = [ErrorException::class])
    fun errorHandler(errorException: ErrorException): WebResponse<Nothing> {
        return WebResponse(
            code = HttpStatus.NOT_ACCEPTABLE.value(),
            message = errorException.message.orEmpty()
        )
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = [NoHandlerFoundException::class])
    fun notFoundHandler(noHandlerFoundException: NoHandlerFoundException): WebResponse<Nothing>{
        return WebResponse(
            code = HttpStatus.NOT_FOUND.value(),
            message = noHandlerFoundException.message.orEmpty(),

        )
    }

    @ResponseStatus(value = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(value = [ConstraintViolationException::class])
    fun validationHandler(constraintViolationException: ConstraintViolationException): WebResponse<Nothing> {
        return WebResponse(
            code = HttpStatus.EXPECTATION_FAILED.value(),
            message = constraintViolationException.message.orEmpty()
        )
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [AuthenticationException::class])
    fun unAuthorizedHandler(authenticationException: javax.naming.AuthenticationException): WebResponse<Nothing>{
        return WebResponse(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = authenticationException.message.orEmpty()
        )
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [JwtException::class])
    fun jwtExceptionHandler (jwtException: JwtException): WebResponse<Nothing> {
        return WebResponse(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = jwtException.message.orEmpty()
        )
    }
}