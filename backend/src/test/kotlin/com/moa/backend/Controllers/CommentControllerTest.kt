package com.moa.backend.Controllers

import TestUtility
import com.moa.backend.controller.CommentController
import com.moa.backend.model.dto.UserDto
import com.moa.backend.repository.CommentRepository
import com.moa.backend.service.CommentService
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
class CommentControllerTest {

    @MockK
    private lateinit var commentService: CommentService

    @MockK
    private lateinit var commentRepository: CommentRepository

    @InjectMockKs
    private lateinit var commentController: CommentController

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getCommentsByIdea(id) should call commentService getCommentsByIdea(id)`() {
        every { commentService.getCommentsByIdea(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.getCommentsByIdea(1)
        verify { commentService.getCommentsByIdea(any()) }
    }

    @Test
    fun `getComment(id) should call commentService getComment(id)`() {
        every { commentService.getComment(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.GetComment(1)
        verify { commentService.getComment(any()) }
    }

    @Test
    fun `createComment(comment) should call commentService createComment(comment)`() {
        every { commentService.createComment(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.createComment(testUtil.createMockCommentDto())
        verify { commentService.createComment(any()) }
    }
    @Test
    fun `editComment(comment) should call commentService editComment(comment)`() {
        every { commentService.editComment(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.editComment(testUtil.createMockSlimCommentDto())
        verify { commentService.editComment(any()) }
    }

    @Test
    fun `likeComment(id) should call commentService likeComment(id)`() {
        every { commentService.likeComment(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.likeComment(1)
        verify { commentService.likeComment(any()) }
    }

    @Test
    fun `dislikeComment(id) should call commentService dislikeComment(id)`() {
        every { commentService.dislikeComment(any()) } returns ResponseEntity.ok(WebResponse<Any>(code = 200, message = "Success", data = listOf<UserDto>()))
        commentController.dislikeComment(1)
        verify { commentService.dislikeComment(any()) }
    }

}