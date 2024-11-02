package com.moa.backend.service

import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userMapper: UserMapper

    private val logger = KotlinLogging.logger {}



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

        val authentication = SecurityContextHolder.getContext().authentication
        if(authentication.authorities.find{ auth -> auth.authority.toString() == "ADMIN"} == null) {
            logger.info { "Unauthorized user ${authentication.name} tried to edit user ${originalUser.email} role" }
            return ResponseEntity.ok(
                WebResponse<UserDto>(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "User is unauthorized to do this action!",
                    data = null
                )
            )
        }


        val originalRole = originalUser.role.toString()
        val newUser = userRepository.saveAndFlush(originalUser)
        if(originalUser.role.toString() != role) {
            originalUser.role = Role.valueOf(role)
        }
        logger.info {"User Permission updated by user: ${authentication.name}. The change in user ${originalUser.email} is ${originalRole} -> ${newUser.role.toString()}."}
        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User SuccessFully updated!",
                data = userMapper.modelToDto(newUser)
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

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.authorities.find{ auth -> auth.authority.toString() == "ADMIN"} == null) {
            if (authentication.name != originalUser.email) {
                logger.info { "Unauthorized user ${authentication.name} tried to edit user ${originalUser.email}" }
                return ResponseEntity.ok(
                    WebResponse<UserDto>(
                        code = HttpStatus.UNAUTHORIZED.value(),
                        message = "User is unauthorized to do this action!",
                        data = null
                    )
                )
            }
        }

        if(!originalUser.firstName.isNullOrEmpty() && originalUser.firstName != user.firstName) {
            originalUser.firstName = user.firstName
        }

        if(!originalUser.lastName.isNullOrEmpty() && originalUser.lastName != user.lastName) {
            originalUser.lastName = user.lastName
        }

        if(!originalUser.email.isNullOrEmpty() && originalUser.email != user.email) {
            originalUser.email = user.email
        }

        val newUser = userRepository.saveAndFlush(originalUser)
        logger.info { "User ${newUser.email} updated by user: ${authentication.name}." }

        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User SuccessFully updated!",
                data = userMapper.modelToDto(newUser)
            )
        )
    }

    fun deleteUser(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication.authorities.find{ auth -> auth.authority.toString() == "ADMIN"} == null) {
            logger.info { "Unauthorized user ${authentication.name} tried to delete user by id ${id}" }
            return ResponseEntity.ok(
                WebResponse<UserDto>(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "User is unauthorized to do this action!",
                    data = null
                )
            )
        }
        val userToDelete = userRepository.findById(id).orElse(null)
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

        logger.info { "User ${userToDelete.email} deleted by user: ${authentication.name}." }
        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "User Successfully Deleted!",
                data = "User Successfully Deleted!"
            )
        )
    }
}