package com.moa.backend.Services


import TestUtility
import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.model.Idea
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.dto.utility.BulkIdeaDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.IdeaService
import com.moa.backend.service.ScoreService
import com.moa.backend.service.UserService
import com.moa.backend.utility.WebResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class ScoreServiceTest {

   @Autowired
   lateinit var scoreService: ScoreService

   @MockkBean
   lateinit var userRepository: UserRepository

   @MockkBean
   lateinit var ideaRepository: IdeaRepository

   @MockkBean
   lateinit var ideaBoxRepository: IdeaBoxRepository

   @MockkBean
   lateinit var ideaService: IdeaService

   @MockkBean
   lateinit var ideaBoxMapper: IdeaBoxMapper

   @MockkBean
   lateinit var ideaMapper: IdeaMapper

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getIdeas() should return list of bulk idea dtos`() {
        val email = "test@example.com"
        val user = mockk<User>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication
        every { authentication.name } returns email
        every { userRepository.findByEmail(email) } returns Optional.of(user)

        val idea1 = mockk<Idea>(relaxed = true)
        val idea2 = mockk<Idea>(relaxed = true)
        val ideaBox = mockk<IdeaBox>(relaxed = true)
        val ideas = listOf(idea1, idea2)

        every { ideaRepository.findByRequiredJuriesContaining(user) } returns ideas
        every { idea1.ideaBox } returns ideaBox
        every { idea2.ideaBox } returns ideaBox

        val ideaBoxDto = mockk<IdeaBoxDto>(relaxed = true)
        val ideaBoxSlimDto = mockk<IdeaBoxSlimDto>(relaxed = true)
        val ideaSlimDtoList = mutableListOf(mockk<IdeaSlimDto>(relaxed = true))

        every { ideaBoxMapper.modelToSlimDto(ideaBox) } returns ideaBoxSlimDto
        every { ideaMapper.ModelListToSlimDto(ideas) } returns ideaSlimDtoList

        val response: ResponseEntity<*> = scoreService.getIdeas()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)

        val expectedBulkIdeaDto = BulkIdeaDto(ideaBoxSlimDto, ideaSlimDtoList)
        assertEquals(listOf(expectedBulkIdeaDto), responseBody.data)

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoredIdeaBoxes() should return list of idea box slim dtos`() {
        val ideaBox1 = mockk<IdeaBox>(relaxed = true)
        val ideaBox2 = mockk<IdeaBox>(relaxed = true)
        val ideaBoxList = listOf(ideaBox1, ideaBox2)

        every { ideaBoxRepository.findIdeaBoxesWithIdeasHavingScoreSheets() } returns ideaBoxList

        val ideaBoxSlimDto1 = mockk<IdeaBoxSlimDto>(relaxed = true)
        val ideaBoxSlimDto2 = mockk<IdeaBoxSlimDto>(relaxed = true)
        every { ideaBoxMapper.ModelListToSlimDto(ideaBoxList) } returns mutableListOf(ideaBoxSlimDto1, ideaBoxSlimDto2)

        val response: ResponseEntity<*> = scoreService.getScoredIdeaBoxes()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)

        val expectedData = listOf(ideaBoxSlimDto1, ideaBoxSlimDto2)
        assertEquals(expectedData, responseBody.data)
    }
}