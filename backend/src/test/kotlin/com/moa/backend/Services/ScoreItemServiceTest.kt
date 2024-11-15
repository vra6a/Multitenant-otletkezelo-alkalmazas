package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Idea
import com.moa.backend.model.ScoreItem
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.User
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.repository.*
import com.moa.backend.service.IdeaService
import com.moa.backend.service.ScoreItemService
import com.moa.backend.service.UserService
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
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class ScoreItemServiceTest {

    @Autowired
    private lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    private lateinit var functions: Functions

    @MockkBean
    lateinit var ideaRepository: IdeaRepository

    @MockkBean
    lateinit var ideaBoxRepository: IdeaBoxRepository

    @MockkBean
    lateinit var ideaService: IdeaService

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var ideaMapper: IdeaMapper

    @MockkBean
    lateinit var scoreItemMapper: ScoreItemMapper

    @MockkBean
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @MockkBean
    lateinit var scoreItemRepository: ScoreItemRepository

    @MockkBean
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var scoreItemService: ScoreItemService

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }



    @Test
    fun `saveScoreSheet() should return unauthorized for non-jury or non-admin user`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val regularUser = mockk<User>(relaxed = true)
        val scoreSheetDto = mockk<ScoreSheetDto>(relaxed = true)

        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)

        every { securityContext.authentication } returns authentication
        every { authentication.name } returns "user@example.com"
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { userRepository.findByEmail("user@example.com") } returns Optional.of(regularUser)

        val response: ResponseEntity<*> = scoreItemService.saveScoreSheet(scoreSheetDto, 1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)

        verify { userRepository.findByEmail("user@example.com") }
        verify(exactly = 0) { ideaRepository.findById(any()) }

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoreSheetsByIdea() should return unauthorized if user does not have ADMIN or JURY role`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = scoreItemService.getScoreSheetsByIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getScoreSheetsByIdea() should return score sheets for authorized user`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("JURY"))

        val idea = mockk<Idea>(relaxed = true)
        val scoreSheetTemplate = mockk<ScoreSheet>(relaxed = true)
        val score = mockk<ScoreItem>(relaxed = true)

        val scoreSheetDto = mockk<ScoreSheetDto>(relaxed = true)

        every { ideaRepository.findById(1L) } returns Optional.of(idea)
        every { idea.ideaBox.scoreSheetTemplates } returns mutableListOf(scoreSheetTemplate)
        every { scoreSheetTemplate.scores } returns mutableListOf(score)
        every { scoreSheetMapper.modelToDto(scoreSheetTemplate) } returns scoreSheetDto
        every { scoreSheetMapper.modelToDto(any<ScoreSheet>()) } returns scoreSheetDto

        val response: ResponseEntity<*> = scoreItemService.getScoreSheetsByIdea(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertNotNull(responseBody.data)
        assertTrue((responseBody.data as MutableList<ScoreSheetDto>).isNotEmpty())

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `GetScoreSheetById() should return unauthorized if user does not have ADMIN or JURY role`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication
        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))

        val response: ResponseEntity<*> = scoreItemService.GetScoreSheetById(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `CreateScoreItem() should successfully create score items for authorized user`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val scoreItemDto1 = mockk<ScoreItemDto>(relaxed = true)
        val scoreItemDto2 = mockk<ScoreItemDto>(relaxed = true)
        val scoreItems = mutableListOf(scoreItemDto1, scoreItemDto2)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("ADMIN"))
        every { authentication.name } returns "admin@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val scoreItem = mockk<ScoreItem>(relaxed = true)
        every { scoreItemMapper.dtoToModel(scoreItemDto1) } returns scoreItem
        every { scoreItemMapper.dtoToModel(scoreItemDto2) } returns scoreItem
        every { scoreItemRepository.save(any()) } returns scoreItem

        val response: ResponseEntity<*> = scoreItemService.CreateScoreItem(scoreItems, 1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Item Added Successfully", responseBody.message)
        assertEquals("ok", responseBody.data)

        verify(exactly = 2) { scoreItemRepository.save(any()) }
    }

    @Test
    fun `AddScoreItemToScoreSheetTemplate() should return UNAUTHORIZED if the user does not have ADMIN or JURY role`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.authorities } returns listOf(SimpleGrantedAuthority("USER"))
        every { authentication.name } returns "regular.user@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val scoreItemSlimDto = mockk<ScoreItemSlimDto>(relaxed = true)

        val response: ResponseEntity<*> = scoreItemService.AddScoreItemToScoreSheetTemplate(1L, scoreItemSlimDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)
    }

}