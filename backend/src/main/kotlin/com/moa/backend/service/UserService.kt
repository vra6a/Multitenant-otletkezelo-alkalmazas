package com.moa.backend.service

import com.moa.backend.converter.UserMapper
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userMapper: UserMapper


    fun getUser(id: Long): ResponseEntity<*> {
        val user = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity("Cannot find User with id $id!", HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(userMapper.modelToDto(user))
    }

    fun getUserByEmail(email: String): User {
        return userRepository.findByEmail(email)

    }

    fun getUsers(): ResponseEntity<MutableList<UserSlimDto>> {
        val users = userRepository.findAll()
        val response: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()

        for( user in users ) {
            user?.let {
                response.add(userMapper.modelToSlimDto(user))
            }
        }
        return ResponseEntity.ok(response)
    }

    fun createUser(user: UserDto): ResponseEntity<*> {
        return ResponseEntity.ok(
            userMapper.modelToDto(
                userRepository.saveAndFlush(
                    userMapper.dtoToModel(user)
                )
            )
        )
    }

    fun updateUser(id: Long, user: UserDto): ResponseEntity<*> {
        val originalUser = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity("Cannot find User with id $id!", HttpStatus.NOT_FOUND)

        if(!originalUser.firstName.isNullOrEmpty() && originalUser.firstName != user.firstName) {
            originalUser.firstName = user.firstName
        }

        if(!originalUser.lastName.isNullOrEmpty() && originalUser.lastName != user.lastName) {
            originalUser.lastName = user.lastName
        }

        if(!originalUser.email.isNullOrEmpty() && originalUser.email != user.email) {
            originalUser.email = user.email
        }

        return ResponseEntity.ok(
            userMapper.modelToDto(
                userRepository.saveAndFlush(originalUser)
            )
        )
    }

    fun deleteUser(id: Long): Any {
        kotlin.runCatching {
            userRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity("Nothing to delete! No User exists with the id $id!", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok()
    }

}