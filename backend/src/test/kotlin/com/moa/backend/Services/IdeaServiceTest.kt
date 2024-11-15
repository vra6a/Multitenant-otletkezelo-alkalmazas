package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.*
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.IdeaService
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
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class IdeaServiceTest {

    @Autowired
    private lateinit var functions: Functions

    @MockkBean
    lateinit var ideaRepository: IdeaRepository

    @MockkBean
    lateinit var ideaBoxRepository: IdeaBoxRepository

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var ideaMapper: IdeaMapper

    @MockkBean
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var ideaService: IdeaService

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getIdea() should return an IdeaDto when called on existing idea`() {
        val ideaId = 1L
        val idea = testUtil.createMockIdea()
        val ideaDto = testUtil.createMockIdeaDto()

        every { ideaRepository.findById(ideaId) } returns Optional.of(idea)
        every { ideaMapper.modelToDto(idea) } returns ideaDto

        val response = ideaService.getIdea(ideaId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body is WebResponse<*>)
        val responseBody = response.body as WebResponse<*>
        assertEquals(ideaDto, responseBody.data)
        assertEquals(HttpStatus.OK.value(), responseBody.code)

        verify { ideaRepository.findById(ideaId) }
        verify { ideaMapper.modelToDto(idea) }
    }

    @Test
    fun `getIdea() should return an Error when called on non-existing idea`() {
        val ideaId = 1L

        every { ideaRepository.findById(ideaId) } returns Optional.empty()

        val response = ideaService.getIdea(ideaId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertTrue(response.body is WebResponse<*>)
        val responseBody = response.body as WebResponse<*>
        assertNull(responseBody.data)
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea with this id $ideaId!", responseBody.message)

        verify { ideaRepository.findById(ideaId) }
    }

    @Test
    fun `getIdeaSlim() should return an IdeaSlimDto when called on existing idea`() {
        val ideaId = 1L
        val idea = testUtil.createMockIdea()
        val ideaDto = testUtil.createMockIdeaDto()
        val ideaSlimDto = testUtil.createMockSlimIdeaDto()

        every { ideaRepository.findById(ideaId) } returns Optional.of(idea)
        every { ideaMapper.modelToSlimDto(idea) } returns ideaSlimDto
        every { ideaMapper.modelToDto(idea) } returns ideaDto

        val response = ideaService.getIdeaSlim(ideaId)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(response.body is WebResponse<*>)
        val responseBody = response.body as WebResponse<*>
        assertEquals(ideaSlimDto, responseBody.data)
        assertEquals(HttpStatus.OK.value(), responseBody.code)

        verify { ideaRepository.findById(ideaId) }
        verify { ideaMapper.modelToSlimDto(idea) }
    }

    @Test
    fun `getIdeaSlim() should return an Error when called on non-existing idea`() {
        val ideaId = 1L

        every { ideaRepository.findById(ideaId) } returns Optional.empty()

        val response = ideaService.getIdeaSlim(ideaId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertTrue(response.body is WebResponse<*>)
        val responseBody = response.body as WebResponse<*>
        assertNull(responseBody.data)
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea with this id $ideaId!", responseBody.message)

        verify { ideaRepository.findById(ideaId) }
    }

    @Test
    fun `getIdeas() should return list of IdeaDto's`() {
        val idea1 = mockk<Idea>()
        val idea2 = mockk<Idea>()
        val ideas = listOf(idea1, idea2)

        val ideaSlimDto1 = mockk<IdeaSlimDto>()
        val ideaSlimDto2 = mockk<IdeaSlimDto>()
        val ideaSlimDtos = listOf(ideaSlimDto1, ideaSlimDto2)

        every { ideaRepository.findAll() } returns ideas

        every { ideaMapper.modelToSlimDto(idea1) } returns ideaSlimDto1
        every { ideaMapper.modelToSlimDto(idea2) } returns ideaSlimDto2

        val response: ResponseEntity<*> = ideaService.getIdeas()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(ideaSlimDtos, webResponse.data)
        assertEquals(HttpStatus.OK.value(), webResponse.code)

        verify(exactly = 1) { ideaRepository.findAll() }
        verify(exactly = 1) { ideaMapper.modelToSlimDto(idea1) }
        verify(exactly = 1) { ideaMapper.modelToSlimDto(idea2) }
    }

    @Test
    fun `getDefaultJuries(id) should return list of default juries for the given idea box id`() {
        val jury1 = mockk<User>()
        val jury2 = mockk<User>()
        val juries = listOf(jury1, jury2)

        val userSlimDto1 = mockk<UserSlimDto>()
        val userSlimDto2 = mockk<UserSlimDto>()
        val userSlimDtos = listOf(userSlimDto1, userSlimDto2)

        val ideaBoxId = 1L

        every { userRepository.getJuriesByIdeaBoxId(ideaBoxId) } returns juries

        every { userMapper.modelToSlimDto(jury1) } returns userSlimDto1
        every { userMapper.modelToSlimDto(jury2) } returns userSlimDto2

        val response: ResponseEntity<*> = ideaService.getDefaultJuries(ideaBoxId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(userSlimDtos, webResponse.data)
        assertEquals(HttpStatus.OK.value(), webResponse.code)

        verify(exactly = 1) { userRepository.getJuriesByIdeaBoxId(ideaBoxId) }
        verify(exactly = 1) { userMapper.modelToSlimDto(jury1) }
        verify(exactly = 1) { userMapper.modelToSlimDto(jury2) }
    }

    @Test
    fun `getReviewedIdeas() should return list of reviewed ideas`() {
        val idea1 = mockk<Idea>()
        val idea2 = mockk<Idea>()
        val ideas = listOf(idea1, idea2)

        val ideaSlimDto1 = mockk<IdeaSlimDto>()
        val ideaSlimDto2 = mockk<IdeaSlimDto>()
        val ideaSlimDtos = listOf(ideaSlimDto1, ideaSlimDto2)

        every { ideaRepository.getReviewedIdeas() } returns ideas

        every { ideaMapper.modelToSlimDto(idea1) } returns ideaSlimDto1
        every { ideaMapper.modelToSlimDto(idea2) } returns ideaSlimDto2

        val response: ResponseEntity<*> = ideaService.getReviewedIdeas()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(ideaSlimDtos, webResponse.data)
        assertEquals(HttpStatus.OK.value(), webResponse.code)

        verify(exactly = 1) { ideaRepository.getReviewedIdeas() }
        verify(exactly = 1) { ideaMapper.modelToSlimDto(idea1) }
        verify(exactly = 1) { ideaMapper.modelToSlimDto(idea2) }
    }

    @Test
    fun `createIdea(idea) should return NOT_FOUND when IdeaBox does not exist`() {
        val ideaDto = mockk<IdeaDto>()
        val saveIdea = mockk<Idea>()
        val ideaBox = mockk<IdeaBox>()

        every { ideaMapper.dtoToModel(ideaDto) } returns saveIdea
        every { saveIdea.ideaBox } returns ideaBox
        every { ideaBox.id } returns 1L

        every { ideaBoxRepository.findById(1L) } returns Optional.empty()
        val response: ResponseEntity<*> = ideaService.createIdea(ideaDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("IdeaBox Not found!", webResponse.message)

        verify(exactly = 1) { ideaMapper.dtoToModel(ideaDto) }
        verify(exactly = 1) { ideaBoxRepository.findById(1L) }
    }

    @Test
    fun `createIdea(idea) should return METHOD_NOT_ALLOWED when IdeaBox is closed`() {
        val ideaDto = mockk<IdeaDto>()
        val saveIdea = mockk<Idea>()
        val ideaBox = mockk<IdeaBox>()

        every { ideaMapper.dtoToModel(ideaDto) } returns saveIdea
        every { saveIdea.ideaBox } returns ideaBox
        every { ideaBox.id } returns 1L
        every { ideaBox.endDate } returns Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

        every { ideaBoxRepository.findById(1L) } returns Optional.of(ideaBox)

        val response: ResponseEntity<*> = ideaService.createIdea(ideaDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), webResponse.code)
        assertEquals("IdeaBox no longer accepts ideas! ${ideaBox.endDate}, ${LocalDate.now()}", webResponse.message)

        verify(exactly = 1) { ideaMapper.dtoToModel(ideaDto) }
        verify(exactly = 1) { ideaBoxRepository.findById(1L) }
    }

    @Test
    fun `createIdea(idea) should create Idea successfully`() {
        val ideaDto = mockk<IdeaDto>()
        val saveIdea = mockk<Idea>()
        val ideaBox = mockk<IdeaBox>()
        val savedIdeaDto = mockk<IdeaDto>()

        every { ideaMapper.dtoToModel(ideaDto) } returns saveIdea
        every { saveIdea.ideaBox } returns ideaBox
        every { ideaBox.id } returns 1L
        every { ideaBox.endDate } returns Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        every { ideaRepository.saveAndFlush(saveIdea) } returns saveIdea
        every { ideaMapper.modelToDto(saveIdea) } returns savedIdeaDto

        every { ideaBoxRepository.findById(1L) } returns Optional.of(ideaBox)

        val response: ResponseEntity<*> = ideaService.createIdea(ideaDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea successfully created!", webResponse.message)
        assertEquals(savedIdeaDto, webResponse.data)

        verify(exactly = 1) { ideaMapper.dtoToModel(ideaDto) }
        verify(exactly = 1) { ideaBoxRepository.findById(1L) }
        verify(exactly = 1) { ideaRepository.saveAndFlush(saveIdea) }
        verify(exactly = 1) { ideaMapper.modelToDto(saveIdea) }
    }



    @Test
    fun `likeIdea(id) should like Idea successfully`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val likesList = mutableListOf<User>()

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { ideaRepository.findById(id) } returns Optional.of(idea)
        every { idea.likes } returns likesList
        every { ideaRepository.saveAndFlush(idea) } returns idea

        val response: ResponseEntity<*> = ideaService.likeIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Liked!", webResponse.message)
        assertEquals("Idea Liked!", webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
        verify { ideaRepository.saveAndFlush(idea) }
    }

    @Test
    fun `likeIdea(id) should return NOT_FOUND when Idea does not exist`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.likeIdea(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find Idea with this id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
    }

    @Test
    fun `dislikeIdea(id) should dislike Idea successfully`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val likesList = mutableListOf(user)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { ideaRepository.findById(id) } returns Optional.of(idea)
        every { idea.likes } returns likesList
        every { ideaRepository.saveAndFlush(idea) } returns idea

        val response: ResponseEntity<*> = ideaService.dislikeIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea disliked!", webResponse.message)
        assertEquals("Idea disliked!", webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
        verify { ideaRepository.saveAndFlush(idea) }
    }

    @Test
    fun `dislikeIdea(id) should return NOT_FOUND when Idea does not exist`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.dislikeIdea(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find Idea with this id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
    }

    @Test
    fun `updateIdea() should return 404 when idea does not exist`() {
        val id = 1L
        val ideaDto = mockk<IdeaDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.updateIdea(id, ideaDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea with this id $id!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaRepository.findById(id) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `updateIdea() should return 401 when user is unauthorized and not the owner`() {
        val id = 1L
        val ideaDto = mockk<IdeaDto>(relaxed = true)
        val originalIdea = mockk<Idea>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "nonowner@example.com"
        every { ideaRepository.findById(id) } returns Optional.of(originalIdea)
        every { originalIdea.owner.email } returns "owner@example.com"

        val response: ResponseEntity<*> = ideaService.updateIdea(id, ideaDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaRepository.findById(id) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `updateIdea() should return 200 when idea is successfully updated by admin`() {
        val id = 1L
        val ideaDto = mockk<IdeaDto>(relaxed = true)
        val originalIdea = mockk<Idea>(relaxed = true)
        val updatedIdea = mockk<Idea>(relaxed = true)
        val updatedIdeaDto = mockk<IdeaDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findById(id) } returns Optional.of(originalIdea)
        every { ideaMapper.modelToDto(any()) } returns updatedIdeaDto
        every { ideaRepository.saveAndFlush(originalIdea) } returns updatedIdea

        val response: ResponseEntity<*> = ideaService.updateIdea(id, ideaDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea successfully updated!", responseBody.message)
        assertEquals(updatedIdeaDto, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaRepository.findById(id) }
        verify { ideaRepository.saveAndFlush(originalIdea) }
        verify { ideaMapper.modelToDto(updatedIdea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `updateIdea() should return 200 when idea is successfully updated by owner`() {
        val id = 1L
        val ideaDto = mockk<IdeaDto>(relaxed = true)
        val originalIdea = mockk<Idea>(relaxed = true)
        val updatedIdea = mockk<Idea>(relaxed = true)
        val updatedIdeaDto = mockk<IdeaDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "owner@example.com"
        every { ideaRepository.findById(id) } returns Optional.of(originalIdea)
        every { originalIdea.owner.email } returns "owner@example.com"
        every { ideaMapper.modelToDto(any()) } returns updatedIdeaDto
        every { ideaRepository.saveAndFlush(originalIdea) } returns updatedIdea

        val response: ResponseEntity<*> = ideaService.updateIdea(id, ideaDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea successfully updated!", responseBody.message)
        assertEquals(updatedIdeaDto, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaRepository.findById(id) }
        verify { ideaRepository.saveAndFlush(originalIdea) }
        verify { ideaMapper.modelToDto(updatedIdea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdea() should return 404 when idea does not exist`() {
        val ideaId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findById(ideaId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.deleteIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea with this id $ideaId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaRepository.findById(ideaId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdea() should return 401 when user is unauthorized`() {
        val ideaId = 1L
        val originalIdea = mockk<Idea>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "user@example.com"
        every { ideaRepository.findById(ideaId) } returns Optional.of(originalIdea)
        every { originalIdea.owner.email } returns "owner@example.com"

        val response: ResponseEntity<*> = ideaService.deleteIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaRepository.findById(ideaId) }
        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdea() should return 200 when idea is successfully deleted by admin`() {
        val ideaId = 1L
        val originalIdea = mockk<Idea>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findById(ideaId) } returns Optional.of(originalIdea)
        every { ideaRepository.deleteById(ideaId) } returns Unit

        val response: ResponseEntity<*> = ideaService.deleteIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea successfully deleted!", responseBody.message)
        assertEquals("Idea successfully deleted!", responseBody.data)

        verify { ideaRepository.findById(ideaId) }
        verify { ideaRepository.deleteById(ideaId) }
        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdea() should return 200 when idea is successfully deleted by owner`() {
        val ideaId = 1L
        val originalIdea = mockk<Idea>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "owner@example.com"
        every { ideaRepository.findById(ideaId) } returns Optional.of(originalIdea)
        every { originalIdea.owner.email } returns "owner@example.com"
        every { ideaRepository.deleteById(ideaId) } returns Unit

        val response: ResponseEntity<*> = ideaService.deleteIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea successfully deleted!", responseBody.message)
        assertEquals("Idea successfully deleted!", responseBody.data)

        verify { ideaRepository.findById(ideaId) }
        verify { ideaRepository.deleteById(ideaId) }
        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdea() should return 404 when deletion fails due to repository error`() {
        val ideaId = 1L
        val originalIdea = mockk<Idea>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findById(ideaId) } returns Optional.of(originalIdea)
        every { ideaRepository.deleteById(ideaId) } throws EmptyResultDataAccessException(1)

        val response: ResponseEntity<*> = ideaService.deleteIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Nothing to delete! No Idea exists with the id $ideaId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaRepository.findById(ideaId) }
        verify { ideaRepository.deleteById(ideaId) }
        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeas() should return 401 when user is not ADMIN or JURY`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val user = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "user@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { userRepository.findByEmail("user@example.com") } returns Optional.of(user)
        every { user.email } returns "user@example.com"

        val response: ResponseEntity<*> = ideaService.getScoredIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }
        verify { userRepository.findByEmail("user@example.com") }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeas() should return empty list when there are no ideas and user is ADMIN`() {
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaRepository.findAll() } returns emptyList()
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(testUtil.createMockUser())


        val response: ResponseEntity<*> = ideaService.getScoredIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(emptyList<IdeaSlimDto>(), responseBody.data)

        verify { ideaRepository.findAll() }
        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeas() should return fully scored ideas for JURY`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val scoreSheet1 = mockk<ScoreSheet>(relaxed = true)
        val requiredJury = mockk<User>(relaxed = true)
        val mappedDto = mockk<IdeaSlimDto>(relaxed = true)
        val user = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "jury@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("JURY"))

        every { userRepository.findByEmail("jury@example.com") } returns Optional.of(user)
        every { user.email } returns "jury@example.com"

        every { ideaRepository.findAll() } returns listOf(idea)
        every { idea.requiredJuries } returns mutableListOf(requiredJury)
        every { requiredJury.email } returns "jury@example.com"
        every { idea.scoreSheets } returns mutableListOf(scoreSheet1)
        every { scoreSheet1.templateFor } returns null
        every { scoreSheet1.owner.email } returns "jury@example.com"

        every { ideaMapper.modelToSlimDto(idea) } returns mappedDto

        val response: ResponseEntity<*> = ideaService.getScoredIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(listOf(mappedDto), responseBody.data)

        verify { ideaRepository.findAll() }
        verify { userRepository.findByEmail("jury@example.com") }
        verify { ideaMapper.modelToSlimDto(idea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeas() should not return partially scored ideas for ADMIN`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val scoreSheet1 = mockk<ScoreSheet>(relaxed = true)
        val requiredJury = mockk<User>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)

        every { ideaRepository.findAll() } returns listOf(idea)

        every { idea.requiredJuries } returns mutableListOf(requiredJury)
        every { requiredJury.email } returns "jury@example.com"
        every { idea.scoreSheets } returns mutableListOf(scoreSheet1)
        every { scoreSheet1.templateFor } returns null
        every { scoreSheet1.owner.email } returns "another_jury@example.com"

        val response: ResponseEntity<*> = ideaService.getScoredIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(emptyList<IdeaSlimDto>(), responseBody.data)

        verify { ideaRepository.findAll() }
        verify { securityContext.authentication }
        verify { userRepository.findByEmail("admin@example.com") }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeas() should return only fully scored ideas when there are mixed scored and unscored ideas`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val fullyScoredIdea = mockk<Idea>(relaxed = true)
        val partiallyScoredIdea = mockk<Idea>(relaxed = true)
        val scoreSheet1 = mockk<ScoreSheet>(relaxed = true)
        val requiredJury = mockk<User>(relaxed = true)
        val mappedDto = mockk<IdeaSlimDto>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)
        every { ideaRepository.findAll() } returns listOf(fullyScoredIdea, partiallyScoredIdea)
        every { fullyScoredIdea.requiredJuries } returns mutableListOf(requiredJury)
        every { requiredJury.email } returns "jury@example.com"
        every { fullyScoredIdea.scoreSheets } returns mutableListOf(scoreSheet1)
        every { scoreSheet1.templateFor } returns null
        every { scoreSheet1.owner.email } returns "jury@example.com"
        every { partiallyScoredIdea.requiredJuries } returns mutableListOf(requiredJury)
        every { partiallyScoredIdea.scoreSheets } returns mutableListOf()
        every { ideaMapper.modelToSlimDto(fullyScoredIdea) } returns mappedDto

        val response: ResponseEntity<*> = ideaService.getScoredIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(listOf(mappedDto), responseBody.data)

        verify { userRepository.findByEmail("admin@example.com") }
        verify { ideaRepository.findAll() }
        verify { ideaMapper.modelToSlimDto(fullyScoredIdea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getIdeasToScore() should return ideas for scoring for authorized users`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val authorizedUser = mockk<User>(relaxed = true)
        val requiredIdea = mockk<Idea>(relaxed = true)
        val otherIdea = mockk<Idea>(relaxed = true)
        val scoreSheet = mockk<ScoreSheet>(relaxed = true)
        val mappedDto = mockk<IdeaSlimDto>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "jury@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("JURY"))
        every { userRepository.findByEmail("jury@example.com") } returns Optional.of(authorizedUser)

        every { ideaRepository.findAll() } returns listOf(requiredIdea, otherIdea)
        every { requiredIdea.requiredJuries } returns mutableListOf(authorizedUser)
        every { otherIdea.requiredJuries } returns mutableListOf()
        every { requiredIdea.scoreSheets } returns mutableListOf(scoreSheet)
        every { scoreSheet.owner.email } returns "other.jury@example.com"
        every { ideaMapper.modelToSlimDto(requiredIdea) } returns mappedDto

        val response: ResponseEntity<*> = ideaService.getIdeasToScore()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(listOf(mappedDto), responseBody.data)

        verify { userRepository.findByEmail("jury@example.com") }
        verify { ideaRepository.findAll() }
        verify { ideaMapper.modelToSlimDto(requiredIdea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getIdeasToScore() should return unauthorized for non-admin or non-jury users`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val unauthorizedUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "user@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { userRepository.findByEmail("user@example.com") } returns Optional.of(unauthorizedUser)

        val response: ResponseEntity<*> = ideaService.getIdeasToScore()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)

        verify { userRepository.findByEmail("user@example.com") }
        verify(exactly = 0) { ideaRepository.findAll() }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `approveIdea() should approve idea for admin user`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val mappedDto = mockk<IdeaDto>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)
        every { adminUser.role } returns Role.ADMIN

        every { ideaRepository.findById(1L) } returns Optional.of(idea)
        every { ideaRepository.save(idea) } returns idea
        every { ideaMapper.modelToDto(idea) } returns mappedDto

        val response: ResponseEntity<*> = ideaService.approveIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Was approved successfully", responseBody.message)
        assertEquals(mappedDto, responseBody.data)

        verify { ideaRepository.findById(1L) }
        verify { idea.status = Status.APPROVED }
        verify { ideaRepository.save(idea) }
        verify { ideaMapper.modelToDto(idea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `approveIdea() should return unauthorized for non-admin users`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val nonAdminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "user@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { userRepository.findByEmail("user@example.com") } returns Optional.of(nonAdminUser)

        val response: ResponseEntity<*> = ideaService.approveIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)

        verify { userRepository.findByEmail("user@example.com") }
        verify(exactly = 0) { ideaRepository.findById(any()) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `approveIdea() should return OK with message when idea is not found`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)
        every { adminUser.role } returns Role.ADMIN

        every { ideaRepository.findById(1L) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.approveIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Was approved successfully", responseBody.message)
        assertNull(responseBody.data)

        verify { ideaRepository.findById(1L) }
        verify(exactly = 0) { ideaRepository.save(any()) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `denyIdea() should deny idea for admin user`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val mappedDto = mockk<IdeaDto>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)
        every { adminUser.role } returns Role.ADMIN

        every { ideaRepository.findById(1L) } returns Optional.of(idea)
        every { ideaRepository.save(idea) } returns idea
        every { ideaMapper.modelToDto(idea) } returns mappedDto

        val response: ResponseEntity<*> = ideaService.denyIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Was denied successfully", responseBody.message)
        assertEquals(mappedDto, responseBody.data)

        verify { ideaRepository.findById(1L) }
        verify { idea.status = Status.DENIED }
        verify { ideaRepository.save(idea) }
        verify { ideaMapper.modelToDto(idea) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `denyIdea() should return unauthorized for non-admin users`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val nonAdminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "user@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { userRepository.findByEmail("user@example.com") } returns Optional.of(nonAdminUser)

        val response: ResponseEntity<*> = ideaService.denyIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)

        verify { userRepository.findByEmail("user@example.com") }
        verify(exactly = 0) { ideaRepository.findById(any()) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `denyIdea() should return OK with message when idea is not found`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val adminUser = mockk<User>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "admin@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { userRepository.findByEmail("admin@example.com") } returns Optional.of(adminUser)
        every { adminUser.role } returns Role.ADMIN

        every { ideaRepository.findById(1L) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.denyIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Was denied successfully", responseBody.message)
        assertNull(responseBody.data)

        verify { ideaRepository.findById(1L) }
        verify(exactly = 0) { ideaRepository.save(any()) }

        SecurityContextHolder.clearContext()
    }
}