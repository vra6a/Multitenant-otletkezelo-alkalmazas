package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.ScoreSheetController
import com.moa.backend.controller.TagController
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.repository.TagRepository
import com.moa.backend.service.ScoreItemService
import com.moa.backend.service.TagService
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
class TagControllerTest {

    @MockK
    private lateinit var tagService: TagService

    @MockK
    private lateinit var tagRepository: TagRepository

    @InjectMockKs
    private lateinit var tagController: TagController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getTags() should call tagService getTags()`() {
        every { tagService.getTags() } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        tagController.getTags()
        verify { tagService.getTags() }
    }

    @Test
    fun `getTag(id) should call tagService getTag(id)`() {
        every { tagService.getTag(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        tagController.getTag(1)
        verify { tagService.getTag(any()) }
    }

    @Test
    fun `getTagSlim(id) should call tagService getTagSlim(id)`() {
        every { tagService.getTagSlim(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        tagController.getTagSlim(1)
        verify { tagService.getTagSlim(any()) }
    }
}