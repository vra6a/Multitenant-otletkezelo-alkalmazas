package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.UserController
import com.moa.backend.model.Role
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.UserRepository
import com.moa.backend.security.auth.AuthenticationRequest
import com.moa.backend.security.auth.AuthenticationResponse
import com.moa.backend.security.auth.RegisterRequest
import com.moa.backend.service.AuthenticationService
import com.moa.backend.service.UserService
import com.moa.backend.utility.ErrorException
import com.moa.backend.utility.WebResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@SpringBootTest
@ExtendWith(MockKExtension::class)
class UserControllerTest {

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var authService: AuthenticationService

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var userController: UserController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }


    @Test
    fun `getUsers() should call userService getUsers`() {
        every { userService.getUsers() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        userController.getUsers()
        verify { userService.getUsers() }
    }

    @Test
    fun `getJuries() should call userService getJuries`() {
        every { userService.getJuries() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        userController.getJuries()
        verify { userService.getJuries() }
    }

    @Test
    fun `getUser(id) should call userService getUser(id)`() {
        every { userService.getUser(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        userController.getUser(1)
        verify { userService.getUser(any()) }
    }

    @Test
    fun `getUserByEmail(email) should call userService getUserByEmail(email)`() {
        every { userService.getUserByEmail(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        userController.getUserByEmail("email")
        verify { userService.getUserByEmail(any()) }
    }

    @Test
    fun `register() should register user successfully`() {
        val request = RegisterRequest(email = "test@example.com", password = "password", firstName = "test", lastName = "test")
        every { userRepository.findByEmail(request.email) } returns java.util.Optional.empty()
        every { authService.register(request) } returns AuthenticationResponse("token", id = 1, firstName = "test", lastName = "test", email = "test@example.com", role = Role.ADMIN)

        val response: WebResponse<AuthenticationResponse> = userController.register(request)

        assert(response.code == HttpStatus.OK.value())
        assert(response.message == "User Successfully registered!")
        assert(response.data?.token == "token")

        verify { userRepository.findByEmail(request.email) }
        verify { authService.register(request) }
    }

    @Test
    fun `register() should throw ErrorException if email is already registered`() {
        val request = RegisterRequest(email = "test@example.com", password = "password", firstName = "test", lastName = "test")
        every { userRepository.findByEmail(request.email) } returns java.util.Optional.of(mockk()) // Simulate existing user

        val exception = org.junit.jupiter.api.assertThrows<ErrorException> {
            userController.register(request)
        }
        assert(exception.message == "Email address is already registered!")

        verify { userRepository.findByEmail(request.email) }
        verify(exactly = 0) { authService.register(request) }
    }

    @Test
    fun `login() should authenticate user successfully`() {
        val request = AuthenticationRequest(email = "test@example.com", password = "password")
        val authResponse = AuthenticationResponse("token", id = 1, firstName = "test", lastName = "test", email = "test@example.com", role = Role.ADMIN)

        every { userRepository.findByEmail(request.email) } returns java.util.Optional.of(mockk())
        every { authService.authenticate(request) } returns authResponse

        val response: WebResponse<AuthenticationResponse> = userController.login(request)

        assert(response.code == HttpStatus.OK.value())
        assert(response.message == "Login was successfull!")
        assert(response.data?.token == "token")

        verify { userRepository.findByEmail(request.email) }
        verify { authService.authenticate(request) }
    }

    @Test
    fun `login() should throw ErrorException if email is not found`() {
        val request = AuthenticationRequest(email = "notfound@example.com", password = "password")
        every { userRepository.findByEmail(request.email) } returns java.util.Optional.empty() // Simulate no user found

        val exception = org.junit.jupiter.api.assertThrows<ErrorException> {
            userController.login(request)
        }
        assert(exception.message == "Email address is not found!")

        verify { userRepository.findByEmail(request.email) }
        verify(exactly = 0) { authService.authenticate(request) } // Ensure authenticate was not called
    }

    @Test
    fun `editRole() should call userService editUserRole`() {
        val userId = 1L
        val role = "ADMIN"
        every { userService.editUserRole(userId, role) } returns ResponseEntity.ok(WebResponse<String>(code = 200, message = "Role updated", data = null))

        val response: ResponseEntity<*> = userController.editRole(userId, role)

        assert(response is ResponseEntity<*>)
        assert((response.body as WebResponse<*>).code == HttpStatus.OK.value())
        assert((response.body as WebResponse<*>).message == "Role updated")

        verify { userService.editUserRole(userId, role) }
    }

    @Test
    fun `updateUser() should call userService updateUser`() {
        val userId = 1L
        val userDto = testUtil.createMockUserDto()
        every { userService.updateUser(userId, userDto) } returns ResponseEntity.ok(WebResponse<String>(code = 200, message = "User updated", data = null))

        val response: ResponseEntity<*> = userController.updateUser(userId, userDto)

        assert(response is ResponseEntity<*>)
        assert((response.body as WebResponse<*>).code == HttpStatus.OK.value())
        assert((response.body as WebResponse<*>).message == "User updated")

        verify { userService.updateUser(userId, userDto) }
    }

    @Test
    fun `deleteUser() should call userService deleteUser`() {
        val userId = 1L
        every { userService.deleteUser(userId) } returns ResponseEntity.ok(WebResponse<String>(code = 200, message = "User deleted", data = null))

        val response: ResponseEntity<*> = userController.deleteUser(userId)

        assert(response is ResponseEntity<*>)
        assert((response.body as WebResponse<*>).code == HttpStatus.OK.value())
        assert((response.body as WebResponse<*>).message == "User deleted")

        verify { userService.deleteUser(userId) }
    }
}