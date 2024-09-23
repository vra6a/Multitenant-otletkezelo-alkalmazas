package com.moa.backend.Controllers

import com.moa.backend.controller.ScoreController
import com.moa.backend.model.dto.UserDto
import com.moa.backend.service.ScoreService
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
class ScoreControllerTest {

    @MockK
    private lateinit var scoreService: ScoreService  // Mock the service, not the controller

    @InjectMockKs
    private lateinit var scoreController: ScoreController  // Inject the service into the controller

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `getIdeas() should call scoreService getIdeas()`() {
        // Arrange
        every { scoreService.getIdeas() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))

        // Act
        scoreController.getIdeas()

        // Assert
        verify { scoreService.getIdeas() }
    }

    @Test
    fun `getScoredIdeaBoxes() should call scoreService getScoredIdeaBoxes()`() {
        // Arrange
        every { scoreService.getScoredIdeaBoxes() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))

        // Act
        scoreController.getScoredIdeaBoxes()

        // Assert
        verify { scoreService.getScoredIdeaBoxes() }
    }
}