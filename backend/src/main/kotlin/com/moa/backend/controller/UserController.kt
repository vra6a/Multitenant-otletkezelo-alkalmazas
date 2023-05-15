package com.moa.backend.controller

import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.security.auth.AuthenticationRequest
import com.moa.backend.security.auth.AuthenticationResponse
import com.moa.backend.security.auth.RegisterRequest
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

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authService: AuthenticationService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager



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
    fun register(@RequestBody request: RegisterRequest): WebResponse<AuthenticationResponse> {
        if(userRepository.findByEmail(request.email).isPresent) {
            throw ErrorException("Email address is already registered!")
        }
        return WebResponse(
            code = HttpStatus.OK.value(),
            message = "User Successfully registered!",
            data = authService.register(request)
        )
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: AuthenticationRequest): WebResponse<AuthenticationResponse> {
        if(!userRepository.findByEmail(request.email).isPresent) {
            throw ErrorException("Email address is not found!")
        }
        return WebResponse(
            code = HttpStatus.OK.value(),
            message = "Login was successfull!",
            data = authService.authenticate(request)
        )
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