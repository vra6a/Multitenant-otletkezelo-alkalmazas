package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.*
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.*
import com.moa.backend.service.IdeaBoxService
import com.moa.backend.service.IdeaService
import com.moa.backend.utility.Functions
import com.moa.backend.utility.IdeaScoreSheets
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
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.awt.print.Pageable
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class IdeaBoxServiceTest {

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
    lateinit var ideaBoxMapper: IdeaBoxMapper

    @MockkBean
    lateinit var userMapper: UserMapper

    @MockkBean
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @MockkBean
    lateinit var scoreSheetRepository: ScoreSheetRepository

    @MockkBean
    lateinit var scoreSheetService: ScoreItemRepository

    @MockkBean
    lateinit var securityContext: SecurityContext

    @MockkBean
    lateinit var securityContextHolder: SecurityContextHolder

    @Autowired
    lateinit var ideaBoxService: IdeaBoxService

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getIdeaBox() should return 404 when idea box is not found`() {
        val ideaBoxId = 1L
        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBox(ideaBoxId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea Box with this id $ideaBoxId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaBoxRepository.findById(ideaBoxId) }
    }

    @Test
    fun `getIdeaBox() should return 200 when idea box is found`() {
        val ideaBoxId = 1L
        val ideaBox = mockk<IdeaBox>()
        val ideaBoxDto = mockk<IdeaBoxDto>()

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBoxMapper.modelToDto(ideaBox) } returns ideaBoxDto

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBox(ideaBoxId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(ideaBoxDto, responseBody.data)

        verify { ideaBoxRepository.findById(ideaBoxId) }
        verify { ideaBoxMapper.modelToDto(ideaBox) }
    }

    @Test
    fun `getIdeaBoxSlim() should return 404 when idea box is not found`() {
        val ideaBoxId = 1L
        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBoxSlim(ideaBoxId)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Idea Box with this id $ideaBoxId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaBoxRepository.findById(ideaBoxId) }
    }

    @Test
    fun `getIdeaBoxSlim() should return 200 when idea box is found`() {
        val ideaBoxId = 1L
        val ideaBox = mockk<IdeaBox>()
        val ideaBoxSlimDto = mockk<IdeaBoxSlimDto>()

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBoxMapper.modelToSlimDto(ideaBox) } returns ideaBoxSlimDto

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBoxSlim(ideaBoxId)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(ideaBoxSlimDto, responseBody.data)

        verify { ideaBoxRepository.findById(ideaBoxId) }
        verify { ideaBoxMapper.modelToSlimDto(ideaBox) }
    }

    @Test
    fun `getIdeaBoxes() should return 200 and mapped IdeaBoxSlimDto list`() {
        val searchQuery = "sample"
        val pageable: PageRequest = PageRequest.of(0, 10)

        val ideaBox1 = mockk<IdeaBox>(relaxed = true)
        val ideaBox2 = mockk<IdeaBox>(relaxed = true)
        val ideaBoxes: MutableList<IdeaBox> = emptyList<IdeaBox>().toMutableList()
        ideaBoxes.add(ideaBox1)
        ideaBoxes.add(ideaBox2)

        val ideaBoxSlimDto1 = mockk<IdeaBoxSlimDto>(relaxed = true)
        val ideaBoxSlimDto2 = mockk<IdeaBoxSlimDto>(relaxed = true)

        every { ideaBoxRepository.search(searchQuery, pageable) } returns ideaBoxes
        every { ideaBoxMapper.modelToSlimDto(ideaBox1) } returns ideaBoxSlimDto1
        every { ideaBoxMapper.modelToSlimDto(ideaBox2) } returns ideaBoxSlimDto2
        every { ideaBox1.scoreSheetTemplates.isEmpty() } returns true
        every { ideaBox2.scoreSheetTemplates.isEmpty() } returns false

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBoxes(searchQuery, pageable)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<MutableList<IdeaBoxSlimDto>>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(2, responseBody.data?.size)

        verify { ideaBoxRepository.search(searchQuery, pageable) }
        verify { ideaBoxMapper.modelToSlimDto(ideaBox1) }
        verify { ideaBoxMapper.modelToSlimDto(ideaBox2) }
    }

    @Test
    fun `getIdeaBoxes() should return empty list when no idea boxes are found`() {
        val searchQuery = "non-existent"
        val pageable: PageRequest = PageRequest.of(0, 10)
        val emptyIdeaBoxes: List<IdeaBox> = emptyList()

        every { ideaBoxRepository.search(searchQuery, pageable) } returns emptyIdeaBoxes

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBoxes(searchQuery, pageable)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<MutableList<IdeaBoxSlimDto>>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(0, responseBody.data?.size)

        verify { ideaBoxRepository.search(searchQuery, pageable) }
    }

    @Test
    fun `getIdeaBoxCount should return 200 and the count of IdeaBoxes`() {
        val ideaBoxes = mutableListOf<IdeaBox>(
            testUtil.createMockIdeaBox(),
            testUtil.createMockIdeaBox(),
            testUtil.createMockIdeaBox()
        )
        every { ideaBoxRepository.findAll() } returns ideaBoxes

        val response: ResponseEntity<*> = ideaBoxService.getIdeaBoxCount()

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<Int>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(3, responseBody.data)

        verify { ideaBoxRepository.findAll() }
    }

    @Test
    fun `updateIdeaBox() should return 401 when user is unauthorized`() {
        val ideaBoxId = 1L
        val ideaBoxDto = mockk<IdeaBoxDto>()
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf()
        every { authentication.name } returns "testUser"

        val response: ResponseEntity<*> = ideaBoxService.updateIdeaBox(ideaBoxId, ideaBoxDto)

        println("Authorities: ${authentication.authorities}")

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }
        verify { authentication.authorities }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `updateIdeaBox() should return 404 when idea box is not found`() {
        val ideaBoxId = 1L
        val ideaBoxDto = mockk<IdeaBoxDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.updateIdeaBox(ideaBoxId, ideaBoxDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find IdeaBox with this id $ideaBoxId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }
        verify { authentication.authorities }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `updateIdeaBox() should return 200 when idea box is successfully updated`() {
        val ideaBoxId = 1L
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val ideaBoxDto = mockk<IdeaBoxDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val savedIdeaBox = mockk<IdeaBox>(relaxed = true)
        val savedIdeaBoxDto = mockk<IdeaBoxDto>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBoxRepository.save(ideaBox) } returns savedIdeaBox
        every { ideaBoxMapper.modelToDto(savedIdeaBox) } returns savedIdeaBoxDto

        val response: ResponseEntity<*> = ideaBoxService.updateIdeaBox(ideaBoxId, ideaBoxDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully updated!", responseBody.message)
        assertEquals(savedIdeaBoxDto, responseBody.data)

        verify { securityContext.authentication }
        verify { authentication.authorities }
        verify { ideaBoxRepository.findById(ideaBoxId) }
        verify { ideaBoxRepository.save(ideaBox) }
        verify { ideaBoxMapper.modelToDto(savedIdeaBox) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `ideaBoxReadyToClose() should return 401 when user is unauthorized`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.ideaBoxReadyToClose(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `ideaBoxReadyToClose() should return false when IdeaBox is not ready to close due to end date`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)
        val ideaBox = mockk<IdeaBox>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBox.endDate } returns Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())

        val response: ResponseEntity<*> = ideaBoxService.ideaBoxReadyToClose(ideaBoxId)

        val responseBody = response.body as WebResponse<Boolean>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("IdeaBox is not ready to close because the date", responseBody.message)
        assertEquals(false, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `ideaBoxReadyToClose() should return false when IdeaBox is not ready to close due to idea status`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val idea = mockk<Idea>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBox.endDate } returns Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        every { ideaBox.ideas } returns mutableListOf(idea)
        every { idea.status } returns Status.REVIEWED

        val response: ResponseEntity<*> = ideaBoxService.ideaBoxReadyToClose(ideaBoxId)

        val responseBody = response.body as WebResponse<Boolean>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("IdeaBox is not ready to close because status", responseBody.message)
        assertEquals(false, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `ideaBoxReadyToClose() should return true when IdeaBox is ready to close`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)
        val ideaBox = mockk<IdeaBox>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBox.endDate } returns Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant())
        every { ideaBox.ideas } returns mutableListOf()

        val response: ResponseEntity<*> = ideaBoxService.ideaBoxReadyToClose(ideaBoxId)

        val responseBody = response.body as WebResponse<Boolean>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("IdeaBox ready to close", responseBody.message)
        assertEquals(true, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdeaBox() should return 401 when user is unauthorized`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.deleteIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdeaBox() should return 404 when IdeaBox does not exist`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.deleteById(ideaBoxId) } throws EmptyResultDataAccessException(1)

        val response: ResponseEntity<*> = ideaBoxService.deleteIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<String>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Nothing to delete! No Idea Box exists with the id $ideaBoxId!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.deleteById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `deleteIdeaBox() should return 200 when IdeaBox is successfully deleted`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.deleteById(ideaBoxId) } returns Unit

        val response: ResponseEntity<*> = ideaBoxService.deleteIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<String>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully deleted!", responseBody.message)
        assertEquals("Idea Box successfully deleted!", responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.deleteById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getAverageScoresForIdeaBoxByScore() should return 401 when user is unauthorized`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.getAverageScoresForIdeaBoxByScore(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getAverageScoresForIdeaBoxByScore() should return empty list when IdeaBox does not exist`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.getAverageScoresForIdeaBoxByScore(ideaBoxId)

        val responseBody = response.body as WebResponse<MutableList<IdeaScoreSheets>>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(emptyList<IdeaScoreSheets>(), responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getAverageScoresForIdeaBoxByScore() should return idea scores successfully`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)
        val ideaBox = mockk<IdeaBox>()
        val idea = mockk<Idea>(relaxed = true)
        val scoreSheet = mockk<ScoreSheet>(relaxed = true)
        val ideaSlimDto = mockk<IdeaSlimDto>()
        val scoreSheetsDto = mockk<MutableList<ScoreSheetDto>>()

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBox.ideas } returns mutableListOf(idea)
        every { ideaMapper.modelToSlimDto(idea) } returns ideaSlimDto
        every { scoreSheetMapper.modelListToDto(idea.scoreSheets) } returns scoreSheetsDto

        val response: ResponseEntity<*> = ideaBoxService.getAverageScoresForIdeaBoxByScore(ideaBoxId)

        val responseBody = response.body as WebResponse<MutableList<IdeaScoreSheets>>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(1, responseBody.data?.size)
        assertEquals(IdeaScoreSheets(ideaSlimDto, scoreSheetsDto), responseBody.data?.first())

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }
        verify { ideaMapper.modelToSlimDto(idea) }
        verify { scoreSheetMapper.modelListToDto(idea.scoreSheets) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `areAllIdeasScoredByRequiredJuries() should return false when user is unauthorized`() {
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val result = ideaBoxService.areAllIdeasScoredByRequiredJuries(ideaBox)

        assertFalse(result)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `areAllIdeasScoredByRequiredJuries() should return false when not all required juries have scored`() {
        val ideaBox = mockk<IdeaBox>()
        val idea = mockk<Idea>(relaxed = true)
        val requiredJury = mockk<User>(relaxed = true) {
            every { id } returns 1L
        }
        val missingJury = mockk<User>(relaxed = true) {
            every { id } returns 2L
        }
        val scoreSheet = mockk<ScoreSheet>(relaxed = true) {
            every { owner.id } returns 1L
        }

        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBox.ideas } returns mutableListOf(idea)
        every { idea.requiredJuries } returns mutableListOf(requiredJury, missingJury)
        every { idea.scoreSheets } returns mutableListOf(scoreSheet)

        val result = ideaBoxService.areAllIdeasScoredByRequiredJuries(ideaBox)

        assertFalse(result)

        verify { securityContext.authentication }
        verify { ideaBox.ideas }
        verify { idea.requiredJuries }
        verify { idea.scoreSheets }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `areAllIdeasScoredByRequiredJuries() should return true when all required juries have scored`() {
        val ideaBox = mockk<IdeaBox>()
        val idea = mockk<Idea>(relaxed = true)
        val jury1 = mockk<User>(relaxed = true) {
            every { id } returns 1L
        }
        val jury2 = mockk<User>(relaxed = true) {
            every { id } returns 2L
        }
        val scoreSheet1 = mockk<ScoreSheet>(relaxed = true) {
            every { owner.id } returns 1L
        }
        val scoreSheet2 = mockk<ScoreSheet>(relaxed = true) {
            every { owner.id } returns 2L
        }

        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBox.ideas } returns mutableListOf(idea)
        every { idea.requiredJuries } returns mutableListOf(jury1, jury2)
        every { idea.scoreSheets } returns mutableListOf(scoreSheet1, scoreSheet2)

        val result = ideaBoxService.areAllIdeasScoredByRequiredJuries(ideaBox)

        assertTrue(result)

        verify { securityContext.authentication }
        verify { ideaBox.ideas }
        verify { idea.requiredJuries }
        verify { idea.scoreSheets }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `createScoreSheetTemplate() should return unauthorized when user is not an admin`() {
        val scoreSheetDto = mockk<ScoreSheetDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.createScoreSheetTemplate(scoreSheetDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `createScoreSheetTemplate() should return success when user is admin and score sheet is created`() {
        val scoreSheetDto = mockk<ScoreSheetDto>(relaxed = true)
        val scoreSheet = mockk<ScoreSheet>(relaxed = true)
        val scoreSheetSlimDto = mockk<ScoreSheetSlimDto>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { scoreSheetRepository.saveAndFlush(any()) } returns scoreSheet
        every { scoreSheetMapper.initializeScoreSheet(scoreSheetDto) } returns scoreSheet
        every { scoreSheetMapper.modelToSlimDto(scoreSheet) } returns scoreSheetSlimDto

        val response: ResponseEntity<*> = ideaBoxService.createScoreSheetTemplate(scoreSheetDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("ScoreSheet Created, ready to be filled", responseBody.message)
        assertEquals(scoreSheetSlimDto, responseBody.data)

        verify { securityContext.authentication }
        verify { scoreSheetRepository.saveAndFlush(any()) }
        verify { scoreSheetMapper.initializeScoreSheet(scoreSheetDto) }
        verify { scoreSheetMapper.modelToSlimDto(scoreSheet) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `createIdeaBox() should return unauthorized when user is not an admin`() {
        val ideaBoxDto = mockk<IdeaBoxDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.createIdeaBox(ideaBoxDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `createIdeaBox() should return success when user is admin and idea box is created`() {
        val ideaBoxDto = mockk<IdeaBoxDto>(relaxed = true)
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val ideaBoxDtoResponse = mockk<IdeaBoxDto>(relaxed = true)

        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaBoxMapper.dtoToModel(ideaBoxDto) } returns ideaBox
        every { ideaBoxRepository.save(ideaBox) } returns ideaBox
        every { ideaBoxMapper.modelToDto(ideaBox) } returns ideaBoxDtoResponse

        val response: ResponseEntity<*> = ideaBoxService.createIdeaBox(ideaBoxDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully created!", responseBody.message)
        assertEquals(ideaBoxDtoResponse, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxMapper.dtoToModel(ideaBoxDto) }
        verify { ideaBoxRepository.save(ideaBox) }
        verify { ideaBoxMapper.modelToDto(ideaBox) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `closeIdeaBox() should return unauthorized when user is not an admin`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.closeIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `closeIdeaBox() should close idea box successfully when user is admin`() {
        val ideaBoxId = 1L
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.of(ideaBox)
        every { ideaBoxRepository.saveAndFlush(ideaBox) } returns ideaBox

        val response: ResponseEntity<*> = ideaBoxService.closeIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("IdeaBox was closed successfully", responseBody.message)
        assertEquals("Success!", responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }
        verify { ideaBoxRepository.saveAndFlush(ideaBox) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `closeIdeaBox() should return not found when ideaBox does not exist`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.closeIdeaBox(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("IdeaBox was closed successfully", responseBody.message)
        assertEquals("Success!", responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `checkIfIdeaBoxHasAllRequiredScoreSheets() should return unauthorized when user is not an admin`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.checkIfIdeaBoxHasAllRequiredScoreSheets(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `checkIfIdeaBoxHasAllRequiredScoreSheets() should return false when ideaBox is not found`() {
        val ideaBoxId = 1L
        val authentication = mockk<Authentication>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { ideaBoxRepository.findById(ideaBoxId) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.checkIfIdeaBoxHasAllRequiredScoreSheets(ideaBoxId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals(false, responseBody.data)

        verify { securityContext.authentication }
        verify { ideaBoxRepository.findById(ideaBoxId) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getClosedIdeaBoxes() should return unauthorized when user is not admin`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.getClosedIdeaBoxes()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertNull(responseBody.data)

        verify { securityContext.authentication }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeaCountByIdeaBox() should return count for admin`() {
        val id = 123L
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))

        every { ideaBoxRepository.countScoredIdeasByIdeaBoxId(id) } returns 5L

        val response: ResponseEntity<*> = ideaBoxService.getScoredIdeaCountByIdeaBox(id)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("5", responseBody.data)
        assertEquals("", responseBody.message)

        verify { ideaBoxRepository.countScoredIdeasByIdeaBoxId(id) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeaCountByIdeaBox() should return unauthorized for non-admin user`() {
        val id = 123L
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = ideaBoxService.getScoredIdeaCountByIdeaBox(id)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("User is unauthorized to do this action!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify(exactly = 0) { ideaBoxRepository.countScoredIdeasByIdeaBoxId(id) }

        SecurityContextHolder.clearContext()
    }
}