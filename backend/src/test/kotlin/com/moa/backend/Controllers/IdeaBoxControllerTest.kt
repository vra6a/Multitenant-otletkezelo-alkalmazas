package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.IdeaBoxController
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.service.IdeaBoxService
import com.moa.backend.utility.WebResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity

@SpringBootTest
@ExtendWith(MockKExtension::class)
class IdeaBoxControllerTest {

    @MockK
    private lateinit var ideaBoxService: IdeaBoxService

    @MockK
    private lateinit var ideaBoxRepository: IdeaBoxRepository

    @InjectMockKs
    private lateinit var ideaBoxController: IdeaBoxController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getIdeaBoxes() should call ideaBoxService getIdeaBoxes() sort on closing`() {
        val search = ""
        val sort = "closing"
        val page = 3
        val items = 5
        every { ideaBoxService.getIdeaBoxes(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getIdeaBoxes(search, sort, page, items)
        verify { ideaBoxService.getIdeaBoxes(any(), any()) }
    }

    @Test
    fun `getIdeaBoxes() should call ideaBoxService getIdeaBoxes() sort on newest`() {
        val search = ""
        val sort = "newest"
        val page = 3
        val items = 5
        every { ideaBoxService.getIdeaBoxes(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getIdeaBoxes(search, sort, page, items)
        verify { ideaBoxService.getIdeaBoxes(any(), any()) }
    }

    @Test
    fun `getIdeaBoxes() should call ideaBoxService getIdeaBoxes() sort on oldest`() {
        val search = ""
        val sort = "oldest"
        val page = 3
        val items = 5
        every { ideaBoxService.getIdeaBoxes(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getIdeaBoxes(search, sort, page, items)
        verify { ideaBoxService.getIdeaBoxes(any(), any()) }
    }

    @Test
    fun `getIdeaBoxCount() should call ideaBoxService getIdeaBoxCount()`() {
        every { ideaBoxService.getIdeaBoxCount() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getIdeaBoxCount()
        verify { ideaBoxService.getIdeaBoxCount() }
    }

    @Test
    fun `getIdeaBox() should call ideaBoxService getIdeaBox()`() {
        every { ideaBoxService.getIdeaBox(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        val test = ideaBoxController.getIdeaBox(1)
        verify { ideaBoxService.getIdeaBox(any()) }
    }

    @Test
    fun `getScoredIdeaCountByIdeaBox() should call ideaBoxService getScoredIdeaCountByIdeaBox()`() {
        every { ideaBoxService.getScoredIdeaCountByIdeaBox(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getScoredIdeaCountByIdeaBox(1)
        verify { ideaBoxService.getScoredIdeaCountByIdeaBox(any()) }
    }

    @Test
    fun `getIdeaBoxSlim() should call ideaBoxService getIdeaBoxSlim()`() {
        every { ideaBoxService.getIdeaBoxSlim(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getIdeaBoxSlim(1)
        verify { ideaBoxService.getIdeaBoxSlim(any()) }
    }

    @Test
    fun `createIdeaBox() should call ideaBoxService createIdeaBox()`() {
        every { ideaBoxService.createIdeaBox(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.createIdeaBox(testUtil.createMockIdeaBoxDto())
        verify { ideaBoxService.createIdeaBox(any()) }
    }

    @Test
    fun `createScoreSheetTemplate() should call ideaBoxService createScoreSheetTemplate()`() {
        every { ideaBoxService.createScoreSheetTemplate(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.createScoreSheetTemplate(testUtil.createMockScoreSheetDto())
        verify { ideaBoxService.createScoreSheetTemplate(any()) }
    }

    @Test
    fun `updateIdeaBox() should call ideaBoxService updateIdeaBox()`() {
        every { ideaBoxService.updateIdeaBox(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.updateIdeaBox(1, testUtil.createMockIdeaBoxDto())
        verify { ideaBoxService.updateIdeaBox(any(), any()) }
    }

    @Test
    fun `deleteIdeaBox() should call ideaBoxService deleteIdeaBox()`() {
        every { ideaBoxService.deleteIdeaBox(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.deleteIdeaBox(1)
        verify { ideaBoxService.deleteIdeaBox(any()) }
    }

    @Test
    fun `checkIfIdeaBoxHasAllRequiredScoreSheets() should call ideaBoxService checkIfIdeaBoxHasAllRequiredScoreSheets()`() {
        every { ideaBoxService.checkIfIdeaBoxHasAllRequiredScoreSheets(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.checkIfIdeaBoxHasAllRequiredScoreSheets(1)
        verify { ideaBoxService.checkIfIdeaBoxHasAllRequiredScoreSheets(any()) }
    }

    @Test
    fun `getAverageScoresForIdeaBoxByScore() should call ideaBoxService getAverageScoresForIdeaBoxByScore()`() {
        every { ideaBoxService.getAverageScoresForIdeaBoxByScore(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getAverageScoresForIdeaBoxByScore(1)
        verify { ideaBoxService.getAverageScoresForIdeaBoxByScore(any()) }
    }

    @Test
    fun `ideaBoxReadyToClose() should call ideaBoxService ideaBoxReadyToClose()`() {
        every { ideaBoxService.ideaBoxReadyToClose(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.ideaBoxReadyToClose(1)
        verify { ideaBoxService.ideaBoxReadyToClose(any()) }
    }

    @Test
    fun `closeIdeaBox() should call ideaBoxService closeIdeaBox()`() {
        every { ideaBoxService.closeIdeaBox(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.closeIdeaBox(1)
        verify { ideaBoxService.closeIdeaBox(any()) }
    }

    @Test
    fun `getClosedIdeaBoxes() should call ideaBoxService getClosedIdeaBoxes()`() {
        every { ideaBoxService.getClosedIdeaBoxes() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaBoxController.getClosedIdeaBoxes()
        verify { ideaBoxService.getClosedIdeaBoxes() }
    }
}