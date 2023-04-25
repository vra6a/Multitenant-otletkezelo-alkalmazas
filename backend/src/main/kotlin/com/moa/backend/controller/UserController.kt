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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api/auth")
class UserController(private val userRepository: UserRepository) {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var authService: AuthenticationService

    @Autowired
    lateinit var authenticationManager: AuthenticationManager



    @GetMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUsers(): ResponseEntity<MutableList<UserSlimDto>> {
        return userService.getUsers()
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<*> {
        return  userService.getUser(id)
    }


    @PostMapping("/user")
    fun createUser(@RequestBody user: UserDto): ResponseEntity<*> {
        return userService.createUser(user)
    }

    @PutMapping("/user/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: UserDto): ResponseEntity<*> {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: Long): Any {
        return userService.deleteUser(id)
    }

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("/authenticate")
    fun register(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authService.authenticate(request))
    }
}