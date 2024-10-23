package com.moa.backend.service

import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userMapper: UserMapper

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun getUser(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.find(User::class.java, id)
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User::class.java)
            .setParameter("email", email)
            .resultList
            .firstOrNull()
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val users = currentEntityManager.createQuery("SELECT u FROM User u", User::class.java)
            .resultList

        val response: MutableList<UserSlimDto> = users.map { userMapper.modelToSlimDto(it) }.toMutableList()

        return ResponseEntity.ok(
            WebResponse<MutableList<UserSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }


    fun getJuries(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val users = currentEntityManager.createQuery("SELECT u FROM User u WHERE u.role = :role", User::class.java)
            .setParameter("role", Role.JURY)
            .resultList

        val response: MutableList<UserSlimDto> = users.map { userMapper.modelToSlimDto(it) }.toMutableList()

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

        val newRole = try {
            Role.valueOf(role)
        } catch (e: IllegalArgumentException) {
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.BAD_REQUEST.value(),
                    message = "Invalid role: $role",
                    data = null
                ),
                HttpStatus.BAD_REQUEST
            )
        }

        if (originalUser.role != newRole) {
            originalUser.role = newRole
            userRepository.saveAndFlush(originalUser)
        }

        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User successfully updated!",
                data = userMapper.modelToDto(originalUser)
            )
        )
    }


    fun updateUser(id: Long, user: UserDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val originalUser = currentEntityManager.find(User::class.java, id)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find User with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        user.firstName?.let { if (it != originalUser.firstName) originalUser.firstName = it }
        user.lastName?.let { if (it != originalUser.lastName) originalUser.lastName = it }
        user.email?.let { if (it != originalUser.email) originalUser.email = it }

        currentEntityManager.merge(originalUser)

        return ResponseEntity.ok(
            WebResponse<UserDto>(
                code = HttpStatus.OK.value(),
                message = "User successfully updated!",
                data = userMapper.modelToDto(originalUser)
            )
        )
    }

    fun deleteUser(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val originalUser = currentEntityManager.find(User::class.java, id)
            ?: return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No User exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        currentEntityManager.remove(originalUser)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "User successfully deleted!",
                data = "User successfully deleted!"
            )
        )
    }
}