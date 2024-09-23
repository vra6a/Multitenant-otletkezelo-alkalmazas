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
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
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
    fun `updateIdea(id, idea) should return NOT_FOUND when Idea does not exist`() {
        val id = 1L
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.updateIdea(id, mockk())

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Cannot find Idea with this id $id!", webResponse.message)

        verify { ideaRepository.findById(id) }
    }

    @Test
    fun `updateIdea(id, idea) should update Idea successfully`() {
        val id = 1L
        val originalIdea = mockk<Idea>(relaxed = true)
        val updatedIdeaDto = mockk<IdeaDto>()
        val updatedIdeaDtoResult = mockk<IdeaDto>()

        every { ideaRepository.findById(id) } returns Optional.of(originalIdea)
        every { ideaMapper.dtoToModel(updatedIdeaDto) } returns originalIdea
        every { originalIdea.title } returns "Old Title"
        every { updatedIdeaDto.title } returns "New Title"
        every { originalIdea.description } returns "Old Description"
        every { updatedIdeaDto.description } returns "New Description"
        every { originalIdea.status } returns Status.SUBMITTED
        every { updatedIdeaDto.status } returns Status.APPROVED
        every { updatedIdeaDto.tags } returns mutableListOf<TagSlimDto>()
        every { ideaRepository.saveAndFlush(originalIdea) } returns originalIdea
        every { ideaMapper.modelToDto(originalIdea) } returns updatedIdeaDtoResult

        val response: ResponseEntity<*> = ideaService.updateIdea(id, updatedIdeaDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea successfully updated!", webResponse.message)
        assertEquals(updatedIdeaDtoResult, webResponse.data)

        verify { ideaRepository.findById(id) }
        verify { originalIdea.title = "New Title" }
        verify { originalIdea.description = "New Description" }
        verify { originalIdea.status = Status.APPROVED }
        verify { ideaRepository.saveAndFlush(originalIdea) }
        verify { ideaMapper.modelToDto(originalIdea) }
    }

    @Test
    fun `deleteIdea(id) should delete Idea successfully`() {
        val id = 1L

        every { ideaRepository.deleteById(id) } returns Unit

        val response: ResponseEntity<*> = ideaService.deleteIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea successfully deleted!", webResponse.message)
        assertEquals("Idea successfully deleted!", webResponse.data)

        verify { ideaRepository.deleteById(id) }
    }

    @Test
    fun `deleteIdea(id) should return NOT_FOUND when Idea does not exist`() {
        val id = 1L

        every { ideaRepository.deleteById(id) } throws Exception("Idea not found")

        val response: ResponseEntity<*> = ideaService.deleteIdea(id)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), webResponse.code)
        assertEquals("Nothing to delete! No Idea exists with the id $id!", webResponse.message)
        assertNull(webResponse.data)

        verify { ideaRepository.deleteById(id) }
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
    fun `getIdeasToScore() should return ideas that the user can score`() {
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val idea1 = mockk<Idea>(relaxed = true)
        val idea2 = mockk<Idea>(relaxed = true)
        val ideas = listOf(idea1, idea2)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { ideaRepository.findAll() } returns ideas
        every { idea1.requiredJuries } returns mutableListOf(user)
        every { idea1.scoreSheets } returns mutableListOf()
        every { idea2.requiredJuries } returns mutableListOf(mockk())
        every { ideaMapper.modelToSlimDto(idea1) } returns mockk()

        val response: ResponseEntity<*> = ideaService.getIdeasToScore()

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertTrue((webResponse.data as List<*>).isNotEmpty())

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findAll() }
        verify { ideaMapper.modelToSlimDto(idea1) }
        verify(exactly = 0) { ideaMapper.modelToSlimDto(idea2) }
    }

    @Test
    fun `approveIdea(id) should approve idea successfully for admin user`() {
        val id = 1L
        val userEmail = "admin@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val ideaDto = mockk<IdeaDto>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.ADMIN
        every { ideaRepository.findById(id) } returns Optional.of(idea)
        every { idea.status } returns Status.SUBMITTED
        every { ideaMapper.modelToDto(idea) } returns ideaDto
        every { ideaRepository.save(idea) } returns idea

        val response: ResponseEntity<*> = ideaService.approveIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was approved successfully", webResponse.message)
        assertEquals(ideaDto, webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
        verify { idea.status = Status.APPROVED }
        verify { ideaRepository.save(idea) }
        verify { ideaMapper.modelToDto(idea) }
    }

    @Test
    fun `approveIdea(id) should return NOT_FOUND when user is not admin`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.USER
        every { ideaRepository.findById(id) } returns Optional.of(idea)

        val response: ResponseEntity<*> = ideaService.approveIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was approved successfully", webResponse.message)
        assertNull(webResponse.data)
    }

    @Test
    fun `approveIdea(id) should return NOT_FOUND when idea does not exist`() {
        val id = 1L
        val userEmail = "admin@example.com"
        val user = mockk<User>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.ADMIN
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.approveIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was approved successfully", webResponse.message)
        assertNull(webResponse.data)
    }

    @Test
    fun `denyIdea(id) should deny idea successfully for admin user`() {
        val id = 1L
        val userEmail = "admin@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)
        val ideaDto = mockk<IdeaDto>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.ADMIN
        every { ideaRepository.findById(id) } returns Optional.of(idea)
        every { idea.status } returns Status.SUBMITTED
        every { ideaMapper.modelToDto(idea) } returns ideaDto
        every { ideaRepository.save(idea) } returns idea

        val response: ResponseEntity<*> = ideaService.denyIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was denied successfully", webResponse.message)
        assertEquals(ideaDto, webResponse.data)

        verify { userRepository.findByEmail(userEmail) }
        verify { ideaRepository.findById(id) }
        verify { idea.status = Status.DENIED }
        verify { ideaRepository.save(idea) }
        verify { ideaMapper.modelToDto(idea) }
    }

    @Test
    fun `denyIdea(id) should return NOT_FOUND when user is not admin`() {
        val id = 1L
        val userEmail = "user@example.com"
        val user = mockk<User>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.USER
        every { ideaRepository.findById(id) } returns Optional.of(idea)

        val response: ResponseEntity<*> = ideaService.denyIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was denied successfully", webResponse.message)
        assertNull(webResponse.data)
    }

    @Test
    fun `denyIdea(id) should return NOT_FOUND when idea does not exist`() {
        val id = 1L
        val userEmail = "admin@example.com"
        val user = mockk<User>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        every { authentication.name } returns userEmail
        SecurityContextHolder.getContext().authentication = authentication

        every { userRepository.findByEmail(userEmail) } returns Optional.of(user)
        every { user.role } returns Role.ADMIN
        every { ideaRepository.findById(id) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaService.denyIdea(id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Idea Was denied successfully", webResponse.message)
        assertNull(webResponse.data)
    }
}