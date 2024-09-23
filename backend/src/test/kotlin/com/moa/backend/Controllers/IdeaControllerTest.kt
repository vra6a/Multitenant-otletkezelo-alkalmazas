package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.IdeaController
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.service.IdeaService
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
class IdeaControllerTest {

    @MockK
    private lateinit var ideaService: IdeaService

    @MockK
    private lateinit var scoreSheetService: ScoreItemService

    @MockK
    private lateinit var ideaRepository: IdeaRepository

    @InjectMockKs
    private lateinit var ideaController: IdeaController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getIdeas() should call ideaService getIdeas()`() {
        every { ideaService.getIdeas() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getIdeas()
        verify { ideaService.getIdeas() }
    }

    @Test
    fun `getReviewedIdeas() should call ideaService getReviewedIdeas()`() {
        every { ideaService.getReviewedIdeas() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getReviewedIdeas()
        verify { ideaService.getReviewedIdeas() }
    }

    @Test
    fun `getIdea(id) should call ideaService getIdea(id)`() {
        every { ideaService.getIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getIdea(1)
        verify { ideaService.getIdea(any()) }
    }

    @Test
    fun `getDefaultJuries(id) should call ideaService getDefaultJuries(id)`() {
        every { ideaService.getDefaultJuries(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getDefaultJuries(1)
        verify { ideaService.getDefaultJuries(any()) }
    }

    @Test
    fun `getIdeaSlim(id) should call ideaService getIdeaSlim(id)`() {
        every { ideaService.getIdeaSlim(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getIdeaSlim(1)
        verify { ideaService.getIdeaSlim(any()) }
    }

    @Test
    fun `getIdeasToScore() should call ideaService getIdeasToScore()`() {
        every { ideaService.getIdeasToScore() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getIdeasToScore()
        verify { ideaService.getIdeasToScore() }
    }

    @Test
    fun `getScoredIdeas() should call ideaService getScoredIdeas()`() {
        every { ideaService.getScoredIdeas() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getScoredIdeas()
        verify { ideaService.getScoredIdeas() }
    }

    @Test
    fun `getScoreSheetsByIdea(id) should call ideaService getScoreSheetsByIdea(id)`() {
        every { scoreSheetService.getScoreSheetsByIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.getScoreSheetsByIdea(1)
        verify { scoreSheetService.getScoreSheetsByIdea(any()) }
    }

    @Test
    fun `createIdea(idea) should call ideaService getScoreSheetsByIdea(idea)`() {
        every { ideaService.createIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.createIdea(testUtil.createMockIdeaDto())
        verify { ideaService.createIdea(any()) }
    }

    @Test
    fun `updateIdea(id, idea) should call ideaService updateIdea(id, idea)`() {
        every { ideaService.updateIdea(any(), any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.updateIdea(1, testUtil.createMockIdeaDto())
        verify { ideaService.updateIdea(any(), any()) }
    }

    @Test
    fun `deleteIdea(id) should call ideaService deleteIdea(id)`() {
        every { ideaService.deleteIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.deleteIdea(1)
        verify { ideaService.deleteIdea(any()) }
    }

    @Test
    fun `likeIdea(id) should call ideaService likeIdea(id)`() {
        every { ideaService.likeIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.likeIdea(1)
        verify { ideaService.likeIdea(any()) }
    }

    @Test
    fun `dislikeIdea(id) should call ideaService dislikeIdea(id)`() {
        every { ideaService.dislikeIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.dislikeIdea(1)
        verify { ideaService.dislikeIdea(any()) }
    }

    @Test
    fun `approveIdea(id) should call ideaService approveIdea(id)`() {
        every { ideaService.approveIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.approveIdea(1)
        verify { ideaService.approveIdea(any()) }
    }

    @Test
    fun `denyIdea(id) should call ideaService denyIdea(id)`() {
        every { ideaService.denyIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        ideaController.denyIdea(1)
        verify { ideaService.denyIdea(any()) }
    }
}