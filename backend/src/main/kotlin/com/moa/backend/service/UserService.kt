package com.moa.backend.service

import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
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
            ?: return ResponseEntity(
                    WebResponse(
                        code = HttpStatus.NOT_FOUND.value(),
                        message = "Cannot find User with this id $id!",
                        data = null
                    ),
                    HttpStatus.NOT_FOUND
                )

        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = userMapper.modelToDto(user)
            )
        )
    }

    fun getUserByEmail(email: String): ResponseEntity<*> {
        val user = userRepository.findByEmail(email).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find User with this email: $email!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = userMapper.modelToDto(user)
            )
        )
    }

    fun getUsers(): ResponseEntity<*> {
        val users = userRepository.findAll()
        val response: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()

        for( user in users ) {
            user?.let {
                response.add(userMapper.modelToSlimDto(user))
            }
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<UserSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getJuries(): ResponseEntity<*> {
        val users = userRepository.findJuries()
        val response: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()

        for( user in users ) {
            user.let {
                response.add(userMapper.modelToSlimDto(user))
            }
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<UserSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun editUserRole(id: Long, role: String): ResponseEntity<*> {
        val originalUser = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find User with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        if(originalUser.role.toString() != role) {
            originalUser.role = Role.valueOf(role)
        }
        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User SuccessFully updated!",
                data = userMapper.modelToDto(userRepository.saveAndFlush(originalUser))
            )
        )
    }

    fun updateUser(id: Long, user: UserDto): ResponseEntity<*> {
        val originalUser = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find User with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

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
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User SuccessFully updated!",
                data = userMapper.modelToDto(userRepository.saveAndFlush(originalUser))
            )
        )
    }

    fun deleteUser(id: Long): ResponseEntity<*> {
        kotlin.runCatching {
            userRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No User exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "User Successfully Deleted!",
                data = "User Successfully Deleted!"
            )
        )
    }




}