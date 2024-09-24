package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.*
import com.moa.backend.service.IdeaBoxService
import com.moa.backend.service.IdeaService
import com.moa.backend.utility.Functions
import com.moa.backend.utility.WebResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.awt.print.Pageable
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
    fun `createIdeaBox should save IdeaBox and return 200 with created IdeaBoxDto`() {
        val ideaBoxDto = testUtil.createMockIdeaBoxDto()
        val ideaBox = testUtil.createMockIdeaBox()
        val savedIdeaBox = ideaBox.copy(id = 1L)

        every { ideaBoxMapper.dtoToModel(ideaBoxDto) } returns ideaBox
        every { ideaBoxMapper.modelToDto(savedIdeaBox) } returns ideaBoxDto

        every { ideaBoxRepository.save(ideaBox) } returns savedIdeaBox

        val response: ResponseEntity<*> = ideaBoxService.createIdeaBox(ideaBoxDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<IdeaBoxDto>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully created!", responseBody.message)
        assertEquals(ideaBoxDto, responseBody.data)

        verify { ideaBoxMapper.dtoToModel(ideaBoxDto) }
        verify { ideaBoxRepository.save(ideaBox) }
        verify { ideaBoxMapper.modelToDto(savedIdeaBox) }
    }

    @Test
    fun `updateIdeaBox should update the IdeaBox and return 200 with updated IdeaBoxDto`() {
        val ideaBoxDto = testUtil.createMockIdeaBoxDto()
        val originalIdeaBox = testUtil.createMockIdeaBox()
        val updatedIdeaBox = originalIdeaBox.copy(
            name = "Updated IdeaBox",
            description = "Updated Description"
        )

        every { ideaBoxRepository.findById(1L) } returns Optional.of(originalIdeaBox)
        every { ideaBoxRepository.save(originalIdeaBox) } returns updatedIdeaBox

        every { ideaBoxMapper.modelToDto(updatedIdeaBox) } returns ideaBoxDto

        val response: ResponseEntity<*> = ideaBoxService.updateIdeaBox(1L, ideaBoxDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<IdeaBoxDto>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully updated!", responseBody.message)
        assertEquals(ideaBoxDto, responseBody.data)

        verify { ideaBoxRepository.findById(1L) }
        verify { ideaBoxRepository.save(originalIdeaBox) }

        verify { ideaBoxMapper.modelToDto(updatedIdeaBox) }
    }

    @Test
    fun `updateIdeaBox should return 404 if IdeaBox not found`() {
        every { ideaBoxRepository.findById(1L) } returns Optional.empty()

        val response: ResponseEntity<*> = ideaBoxService.updateIdeaBox(1L, testUtil.createMockIdeaBoxDto())

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find IdeaBox with this id 1!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaBoxRepository.findById(1L) }
    }

    @Test
    fun `deleteIdeaBox should return 200 when deletion is successful`() {
        every { ideaBoxRepository.deleteById(1L) } returns Unit

        val response: ResponseEntity<*> = ideaBoxService.deleteIdeaBox(1L)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<String>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Idea Box successfully deleted!", responseBody.message)
        assertEquals("Idea Box successfully deleted!", responseBody.data)

        verify { ideaBoxRepository.deleteById(1L) }
    }

    @Test
    fun `deleteIdeaBox should return 404 when IdeaBox does not exist`() {
        every { ideaBoxRepository.deleteById(1L) } throws RuntimeException("IdeaBox not found")

        val response: ResponseEntity<*> = ideaBoxService.deleteIdeaBox(1L)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Nothing to delete! No Idea Box exists with the id 1!", responseBody.message)
        assertEquals(null, responseBody.data)

        verify { ideaBoxRepository.deleteById(1L) }
    }

    @Test
    fun `createScoreSheetTemplate should return 200 when scoreSheet is created successfully`() {
        val scoreSheetDto = testUtil.createMockScoreSheetDto()
        val scoreSheet = testUtil.createMockScoreSheet()
        val scoreSheetSlimDto = testUtil.createMockSlimScoreSheetDto()

        every { scoreSheetMapper.initializeScoreSheet(scoreSheetDto) } returns scoreSheet
        every { scoreSheetRepository.saveAndFlush(scoreSheet) } returns scoreSheet
        every { scoreSheetMapper.modelToSlimDto(scoreSheet) } returns scoreSheetSlimDto

        val response: ResponseEntity<*> = ideaBoxService.createScoreSheetTemplate(scoreSheetDto)

        assertEquals(HttpStatus.OK, response.statusCode)
        val responseBody = response.body as WebResponse<ScoreSheetSlimDto>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("ScoreSheet Created, ready to be filled", responseBody.message)
        assertEquals(scoreSheetSlimDto, responseBody.data)

        verify { scoreSheetMapper.initializeScoreSheet(scoreSheetDto) }
        verify { scoreSheetRepository.saveAndFlush(scoreSheet) }
        verify { scoreSheetMapper.modelToSlimDto(scoreSheet) }
    }
}