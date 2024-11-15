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
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
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
    fun `updateUser() should return NOT_FOUND if the user does not exist`() {
        val userDto = testUtil.createMockUserDto()
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { userRepository.findById(1L) } returns Optional.empty()
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = userService.updateUser(1L, userDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find User with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `updateUser() should return UNAUTHORIZED if the user is not authorized to update`() {
        val userDto = testUtil.createMockUserDto()
        val originalUser = mockk<User>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { userRepository.findById(1L) } returns Optional.of(originalUser)
        every { originalUser.email } returns "user@test.com" // Original user's email
        every { authentication.name } returns "another.user@example.com"  // Unauthorized user
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = userService.updateUser(1L, userDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `updateUser() should successfully update user when authorized`() {
        val userDto = testUtil.createMockUserDto()
        val originalUser = mockk<User>(relaxed = true)
        val updatedUser = mockk<User>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val userDtoResponse = mockk<UserDto>(relaxed = true)

        every { userRepository.findById(1L) } returns Optional.of(originalUser)
        every { originalUser.email } returns "user@test.com"
        every { originalUser.firstName = userDto.firstName } returns Unit
        every { originalUser.lastName = userDto.lastName } returns Unit
        every { originalUser.email = userDto.email } returns Unit
        every { userRepository.saveAndFlush(originalUser) } returns updatedUser
        every { userMapper.modelToDto(updatedUser) } returns userDtoResponse
        every { authentication.name } returns "user@test.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = userService.updateUser(1L, userDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("User SuccessFully updated!", responseBody.message)
        assertEquals(userDtoResponse, responseBody.data)

        verify { userRepository.saveAndFlush(originalUser) }
    }

    @Test
    fun `deleteUser() should return UNAUTHORIZED if the user is not an admin`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "regular.user@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = userService.deleteUser(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `deleteUser() should return NOT_FOUND if the user does not exist`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { authentication.name } returns "admin@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { userRepository.findById(1L) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.deleteUser(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Nothing to delete! No User exists with the id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `deleteUser() should successfully delete user when authorized`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val userToDelete = mockk<User>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { authentication.name } returns "admin@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { userRepository.findById(1L) } returns Optional.of(userToDelete)
        every { userRepository.deleteById(1L) } returns Unit
        every { userToDelete.email } returns "user.to.delete@example.com"

        val response: ResponseEntity<*> = userService.deleteUser(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("User Successfully Deleted!", responseBody.message)
        assertEquals("User Successfully Deleted!", responseBody.data)

        verify { userRepository.deleteById(1L) }
    }

    @Test
    fun `editUserRole() should return NOT_FOUND if the user is not found`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { authentication.name } returns "admin@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { userRepository.findById(1L) } returns Optional.empty()

        val response: ResponseEntity<*> = userService.editUserRole(1L, "USER")

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find User with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `editUserRole() should return UNAUTHORIZED if the user is not an admin`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val userToEdit = mockk<User>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "regular.user@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { userRepository.findById(1L) } returns Optional.of(userToEdit)
        every { userToEdit.email } returns "user.to.edit@example.com"

        val response: ResponseEntity<*> = userService.editUserRole(1L, "ADMIN")

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `editUserRole() should successfully update the user role when authorized`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val originalUser = mockk<User>(relaxed = true)
        val updatedUser = mockk<User>(relaxed = true)
        val userDto = mockk<UserDto>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { authentication.name } returns "admin@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { userRepository.findById(1L) } returns Optional.of(originalUser)
        every { userRepository.saveAndFlush(originalUser) } returns updatedUser
        every { originalUser.email } returns "user.to.edit@example.com"
        every { originalUser.role } returns Role.USER
        every { updatedUser.role } returns Role.ADMIN
        every { userMapper.modelToDto(updatedUser) } returns userDto

        val response: ResponseEntity<*> = userService.editUserRole(1L, "ADMIN")

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("User SuccessFully updated!", responseBody.message)
        assertEquals(userDto, responseBody.data)

        verify { userRepository.saveAndFlush(originalUser) }
    }

}