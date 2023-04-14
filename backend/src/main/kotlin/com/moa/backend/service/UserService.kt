package com.moa.backend.service

import com.moa.backend.converter.UserConverter
import com.moa.backend.model.User
import com.moa.backend.model.UserListView
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.UserRepository
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    fun getUser(id: Long): UserDto {
        var usermodel = userRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find User with this id!")

        var converter = UserConverter()
        val userDto = converter.convertToDto(usermodel)
        return userDto
    }

    fun getUsers(): List<UserListView> {
        val users = userRepository.findAll()
        return mapToListView(users)
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

    private fun mapToListView(users: List<User>): MutableList<UserListView> {
        val userListView: MutableList<UserListView> = emptyList<UserListView>().toMutableList()
        if(!users.isEmpty()) {
            users.forEach{ user: User ->
                val userlv = UserListView(
                    user.id,
                    user.firstName,
                    user.lastName,
                    user.email,
                    user.role
                )
                userListView.add(userlv)
            }
        }
        return userListView
    }
}