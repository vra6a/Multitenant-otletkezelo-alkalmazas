package com.moa.backend.controller

import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/users")
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
}