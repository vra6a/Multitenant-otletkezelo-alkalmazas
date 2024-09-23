package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Role
import com.moa.backend.model.User
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.UserService
import com.moa.backend.utility.Functions
import com.moa.backend.utility.WebResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.*
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var userService: UserService

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getUser(id) should return user successfully when user exists`() {
        val id = 1L
        val user = mockk<User>(relaxed = true)
        val userDto = mockk<UserDto>(relaxed = true)

        every { userRepository.findById(id) } returns Optional.of(user)
        every { userMapper.modelToDto(user) } returns userDto

        val response: ResponseEntity<*> = userService.getUser(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals(userDto, webResponse.data)

        verify { userRepository.findById(id) }
        verify { userMapper.modelToDto(user) }
    }

    @Test
    fun `getUser(id) should return NOT_FOUND when user does not exist`() {
        val id = 1L

        every { userRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.getUser(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find User with this id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findById(id) }
    }

    @Test
    fun `getUserByEmail(email) should return user successfully when user exists`() {
        val email = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val userDto = mockk<UserDto>(relaxed = true)

        every { userRepository.findByEmail(email) } returns Optional.of(user)
        every { userMapper.modelToDto(user) } returns userDto

        val response: ResponseEntity<*> = userService.getUserByEmail(email)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals(userDto, webResponse.data)

        verify { userRepository.findByEmail(email) }
        verify { userMapper.modelToDto(user) }
    }

    @Test
    fun `getUserByEmail(email) should return NOT_FOUND when user does not exist`() {
        val email = "user@example.com"

        every { userRepository.findByEmail(email) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.getUserByEmail(email)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find User with this email: $email!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findByEmail(email) }
    }

    @Test
    fun `getUsers() should return list of users successfully when users exist`() {
        val user1 = mockk<User>(relaxed = true)
        val user2 = mockk<User>(relaxed = true)
        val userSlimDto1 = mockk<UserSlimDto>(relaxed = true)
        val userSlimDto2 = mockk<UserSlimDto>(relaxed = true)

        every { userRepository.findAll() } returns listOf(user1, user2)
        every { userMapper.modelToSlimDto(user1) } returns userSlimDto1
        every { userMapper.modelToSlimDto(user2) } returns userSlimDto2

        val response: ResponseEntity<*> = userService.getUsers()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals(listOf(userSlimDto1, userSlimDto2), webResponse.data)

        verify { userRepository.findAll() }
        verify { userMapper.modelToSlimDto(user1) }
        verify { userMapper.modelToSlimDto(user2) }
    }

    @Test
    fun `getUsers() should return empty list when no users exist`() {
        every { userRepository.findAll() } returns emptyList()

        val response: ResponseEntity<*> = userService.getUsers()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<MutableList<UserSlimDto>>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        webResponse.data?.let { assertTrue(it.isEmpty()) }

        verify { userRepository.findAll() }
    }

    @Test
    fun `getJuries() should return list of juries successfully when juries exist`() {
        val jury1 = mockk<User>(relaxed = true)
        val jury2 = mockk<User>(relaxed = true)
        val jurySlimDto1 = mockk<UserSlimDto>(relaxed = true)
        val jurySlimDto2 = mockk<UserSlimDto>(relaxed = true)

        every { userRepository.findJuries() } returns listOf(jury1, jury2)
        every { userMapper.modelToSlimDto(jury1) } returns jurySlimDto1
        every { userMapper.modelToSlimDto(jury2) } returns jurySlimDto2

        val response: ResponseEntity<*> = userService.getJuries()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<MutableList<UserSlimDto>>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals(listOf(jurySlimDto1, jurySlimDto2), webResponse.data)

        verify { userRepository.findJuries() }
        verify { userMapper.modelToSlimDto(jury1) }
        verify { userMapper.modelToSlimDto(jury2) }
    }

    @Test
    fun `getJuries() should return empty list when no juries exist`() {
        every { userRepository.findJuries() } returns emptyList()

        val response: ResponseEntity<*> = userService.getJuries()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<MutableList<UserSlimDto>>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        webResponse.data?.let { assertTrue(it.isEmpty()) }

        verify { userRepository.findJuries() }
    }

    @Test
    fun `editUserRole(id, role) should update user role successfully`() {
        val id = 1L
        val newRole = "ADMIN"
        val userEmail = "user@example.com"
        val originalUser = mockk<User>(relaxed = true)
        val updatedUserDto = mockk<UserDto>(relaxed = true)

        every { userRepository.findById(id) } returns Optional.of(originalUser)
        every { originalUser.role } returns Role.USER
        every { userMapper.modelToDto(any()) } returns updatedUserDto
        every { userRepository.saveAndFlush(originalUser) } returns originalUser

        val response: ResponseEntity<*> = userService.editUserRole(id, newRole)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<UserDto>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("User SuccessFully updated!", webResponse.message)
        assertEquals(updatedUserDto, webResponse.data)

        verify { userRepository.findById(id) }
        verify { originalUser.role = Role.ADMIN }
        verify { userRepository.saveAndFlush(originalUser) }
        verify { userMapper.modelToDto(originalUser) }
    }

    @Test
    fun `editUserRole(id, role) should return NOT_FOUND when user does not exist`() {
        val id = 1L
        val role = "ADMIN"

        every { userRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.editUserRole(id, role)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find User with this id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findById(id) }
    }

    @Test
    fun `updateUser(id, user) should update user details successfully`() {
        val id = 1L
        val userDto = testUtil.createMockUserDto()
        val originalUser = mockk<User>(relaxed = false)

        every { userRepository.findById(id) } returns Optional.of(originalUser)

        every { originalUser.firstName } returns "Jane"
        every { originalUser.lastName } returns "Smith"
        every { originalUser.email } returns "jane.smith@example.com"

        every { originalUser.firstName = any() } just Runs
        every { originalUser.lastName = any() } just Runs
        every { originalUser.email = any() } just Runs

        val updatedUserDto = mockk<UserDto>(relaxed = true)
        every { userMapper.modelToDto(originalUser) } returns updatedUserDto
        every { userRepository.saveAndFlush(originalUser) } returns originalUser

        val response: ResponseEntity<*> = userService.updateUser(id, userDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<UserDto>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("User SuccessFully updated!", webResponse.message)
        assertEquals(updatedUserDto, webResponse.data)

        verify { userRepository.findById(id) }
        verify { originalUser.firstName = userDto.firstName }
        verify { originalUser.lastName = userDto.lastName }
        verify { originalUser.email = userDto.email }
        verify { userRepository.saveAndFlush(originalUser) }
        verify { userMapper.modelToDto(originalUser) }
    }

    @Test
    fun `updateUser(id, user) should return NOT_FOUND when user does not exist`() {
        val id = 1L
        val userDto = testUtil.createMockUserDto()

        every { userRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.updateUser(id, userDto)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find User with this id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findById(id) }
    }

    @Test
    fun `deleteUser(id) should delete user successfully`() {
        val id = 1L

        every { userRepository.deleteById(id) } just Runs

        val response: ResponseEntity<*> = userService.deleteUser(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("User Successfully Deleted!", webResponse.message)
        assertEquals("User Successfully Deleted!", webResponse.data)

        verify { userRepository.deleteById(id) }
    }

    @Test
    fun `deleteUser(id) should return NOT_FOUND when user does not exist`() {
        val id = 1L

        every { userRepository.deleteById(id) } throws EmptyResultDataAccessException(404)

        val response: ResponseEntity<*> = userService.deleteUser(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Nothing to delete! No User exists with the id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.deleteById(id) }
    }
}