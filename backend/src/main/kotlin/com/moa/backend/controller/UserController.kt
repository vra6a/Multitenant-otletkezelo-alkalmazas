package com.moa.backend.controller

import com.moa.backend.model.Idea
import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.UserRepository
import com.moa.backend.security.auth.AuthenticationRequest
import com.moa.backend.security.auth.AuthenticationResponse
import com.moa.backend.security.auth.RegisterRequest
import com.moa.backend.security.config.JwtService
import com.moa.backend.service.AuthenticationService
import com.moa.backend.service.UserService
import com.moa.backend.utility.ErrorException
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors
import mu.KotlinLogging
import javax.servlet.http.HttpServletRequest

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authService: AuthenticationService

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    private val logger = KotlinLogging.logger {}



    @GetMapping("/user")
    fun getUsers(): ResponseEntity<*> {
        return userService.getUsers()
    }

    @GetMapping("/user/juries")
    fun getJuries(): ResponseEntity<*> {
        return userService.getJuries()
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<*> {
        return userService.getUser(id)
    }

    @GetMapping("/user/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<*> {
        return userService.getUserByEmail(email)
    }

    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterRequest, httpServletRequest: HttpServletRequest): WebResponse<AuthenticationResponse> {
        if(userRepository.findByEmail(request.email).isPresent) {
            throw ErrorException("Email address is already registered!")
        }
        return WebResponse(
            code = HttpStatus.OK.value(),
            message = "User Successfully registered!",
            data = authService.register(request, httpServletRequest)
        )
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: AuthenticationRequest, requestHeaders: HttpServletRequest): WebResponse<AuthenticationResponse> {
        val user = userRepository.findByEmail(request.email)
        val tenantId = requestHeaders.getHeader("X-Tenant-Id")
        if(!user.isPresent) {
            return WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                message = "Email Not found!",
                data = null
            )
        } else if(user.get().tenantId != tenantId) {
            return WebResponse(
                code = HttpStatus.UNAUTHORIZED.value(),
                message = "Email Not found",
                data = null
            )
        } else {
            logger.info { "MOA-INFO: User with email: ${request.email} tried logging in." }
            return WebResponse(
                code = HttpStatus.OK.value(),
                message = "Login was successfull!",
                data = authService.authenticate(request)
            )
        }
    }

    @PostMapping("/user/{id}/permission")
    fun editRole(@PathVariable id: Long, @RequestBody role: String): ResponseEntity<*> {
        return userService.editUserRole(id, role);
    }

    @PutMapping("/user/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserDto): ResponseEntity<*> {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<*> {
        return userService.deleteUser(id)
    }


}