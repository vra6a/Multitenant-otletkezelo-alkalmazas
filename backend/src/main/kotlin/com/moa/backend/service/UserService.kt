package com.moa.backend.service

import com.moa.backend.model.User
import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(id: Long): User {
        return userRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find User with this id!")
    }

    fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    fun createUser(user: User): User {
        if(user.id != 0L) {
            throw Exception("User with this id ${user.id} already exists!")
        }
        return userRepository.saveAndFlush(user)
    }

    fun updateUser(id: Long, user: User): User {
        val originalUser = userRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find User with this id!")

        if(!originalUser.firstName.isNullOrEmpty() && originalUser.firstName != user.firstName) {
            originalUser.firstName = user.firstName
        }

        if(!originalUser.lastName.isNullOrEmpty() && originalUser.lastName != user.lastName) {
            originalUser.lastName = user.lastName
        }

        if(!originalUser.email.isNullOrEmpty() && originalUser.email != user.email) {
            originalUser.email = user.email
        }

        if( originalUser.role != user.role) {
            originalUser.role = user.role
        }
        return userRepository.saveAndFlush(originalUser)
    }

    fun deleteUser(id: Long): Any {
        kotlin.runCatching {
            userRepository.deleteById(id)
        }.onFailure {
            throw Exception("Nothing to delete! No User exists with the id ${id}.")
        }

        return "OK"
    }


}