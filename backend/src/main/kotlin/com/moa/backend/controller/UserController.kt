package com.moa.backend.controller

import com.moa.backend.repository.UserRepository
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository) {
    //TODO
}