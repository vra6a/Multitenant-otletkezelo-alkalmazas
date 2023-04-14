package com.moa.backend.controller

import com.moa.backend.model.User
import com.moa.backend.model.UserListView
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class UserController(private val userRepository: UserRepository) {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/users")
    fun getUsers(): List<UserListView> {
        return userService.getUsers()
    }

    @GetMapping("/user/{id}")
    fun getUser(@PathVariable id: Long): UserDto {
        return  userService.getUser(id)
    }

    @PostMapping("/user")
    fun createUser(@RequestBody user: User): User {
        return userService.createUser(user)
    }

    @PutMapping("/user/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody user: User): User {
        return userService.updateUser(id, user)
    }

    @DeleteMapping("/user/{id}")
    fun deleteUser(@PathVariable id: Long): Any {
        return userService.deleteUser(id)
    }
}