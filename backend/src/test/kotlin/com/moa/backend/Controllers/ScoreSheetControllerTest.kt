package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.ScoreSheetController
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.service.ScoreItemService
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
class ScoreSheetControllerTest {

    @MockK
    private lateinit var scoreSheetService: ScoreItemService

    @MockK
    private lateinit var scoreSheetRepository: ScoreSheetRepository

    @InjectMockKs
    private lateinit var scoreSheetController: ScoreSheetController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `GetScoreSheetById(id) should call scoreSheetService GetScoreSheetById(id)`() {
        every { scoreSheetService.GetScoreSheetById(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        scoreSheetController.getScoreSheetById(1)
        verify { scoreSheetService.GetScoreSheetById(any()) }
    }

    @Test
    fun `SaveScoreSheet(scoreSheetDto, id) should call scoreSheetService saveScoreSheet(scoreSheet, id)`() {
        every { scoreSheetService.saveScoreSheet(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        scoreSheetController.SaveScoreSheet(testUtil.createMockScoreSheetDto(), 1)
        verify { scoreSheetService.saveScoreSheet(any(), any()) }
    }

    @Test
    fun `CreateScoreItem(tems, id) should call scoreSheetService CreateScoreItem(items, id)`() {
        every { scoreSheetService.CreateScoreItem(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        val list = mutableListOf<ScoreItemDto>()
        list.add(testUtil.createMockScoreItemDto())
        list.add(testUtil.createMockScoreItemDto())
        scoreSheetController.CreateScoreItem(list, 1)
        verify { scoreSheetService.CreateScoreItem(any(), any()) }
    }

    @Test
    fun `AddScoreItemToScoreSheetTemplate(id, item) should call scoreSheetService AddScoreItemToScoreSheetTemplate(id, item)`() {
        every { scoreSheetService.AddScoreItemToScoreSheetTemplate(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        scoreSheetController.AddScoreItemToScoreSheetTemplate(1, testUtil.createMockSlimScoreItemDto())
        verify { scoreSheetService.AddScoreItemToScoreSheetTemplate(any(), any()) }
    }
}