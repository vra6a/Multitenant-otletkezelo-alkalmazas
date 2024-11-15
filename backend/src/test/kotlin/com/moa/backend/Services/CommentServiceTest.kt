package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.CommentMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Comment
import com.moa.backend.model.User
import com.moa.backend.model.dto.CommentDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.repository.CommentRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.service.CommentService
import com.moa.backend.utility.WebResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
class CommentServiceTest {

    @Autowired
    lateinit var commentService: CommentService

    @MockkBean
    lateinit var commentRepository: CommentRepository

    @MockkBean
    lateinit var commentMapper: CommentMapper

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var userMapper: UserMapper

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getComment() should return not found if comment does not exist`() {
        val commentId = 1L

        every { commentRepository.findById(commentId) } returns Optional.empty()

        val response: ResponseEntity<*> = commentService.getComment(commentId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Comment with this id $commentId!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `getComment() should return comment if it exists`() {
        val commentId = 1L
        val comment = mockk<Comment>(relaxed = true)
        val commentDto = mockk<CommentDto>(relaxed = true)

        every { commentRepository.findById(commentId) } returns Optional.of(comment)

        every { commentMapper.modelToDto(comment) } returns commentDto

        val response: ResponseEntity<*> = commentService.getComment(commentId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(commentDto, responseBody.data)
    }

    @Test
    fun `getCommentsByIdea() should return an empty list if no comments are found`() {
        val ideaId = 1L

        every { commentRepository.getCommentsByIdeaId(ideaId) } returns emptyList()

        val response: ResponseEntity<*> = commentService.getCommentsByIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(emptyList<CommentSlimDto>(), responseBody.data)
    }

    @Test
    fun `getCommentsByIdea() should return a list of comments if they exist`() {
        val ideaId = 1L
        val comment1 = mockk<Comment>(relaxed = true)
        val comment2 = mockk<Comment>(relaxed = true)
        val comments = listOf(comment1, comment2)

        val commentSlimDto1 = mockk<CommentSlimDto>(relaxed = true)
        val commentSlimDto2 = mockk<CommentSlimDto>(relaxed = true)

        every { commentRepository.getCommentsByIdeaId(ideaId) } returns comments

        every { commentMapper.modelToSlimDto(comment1) } returns commentSlimDto1
        every { commentMapper.modelToSlimDto(comment2) } returns commentSlimDto2

        val response: ResponseEntity<*> = commentService.getCommentsByIdea(ideaId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(listOf(commentSlimDto1, commentSlimDto2), responseBody.data)
    }

    @Test
    fun `createComment() should return the created comment`() {
        val commentDto = mockk<CommentDto>(relaxed = true)
        val comment = mockk<Comment>(relaxed = true)
        val savedCommentDto = mockk<CommentDto>(relaxed = true)

        every { commentMapper.dtoToModel(commentDto) } returns comment
        every { commentMapper.modelToDto(comment) } returns savedCommentDto

        every { commentRepository.save(comment) } returns comment

        val response: ResponseEntity<*> = commentService.createComment(commentDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Comment successfully created!", responseBody.message)
        assertEquals(savedCommentDto, responseBody.data)
    }

    @Test
    fun `editComment() should fail if the editing user is not the creator`() {
        val commentSlimDto = mockk<CommentSlimDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { commentSlimDto.owner.email } returns "otheruser@example.com"
        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.editComment(commentSlimDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.UNAUTHORIZED.value(), responseBody.code)
        assertEquals("You dont have permission to do that!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `editComment() should fail if the comment is not found`() {
        val commentSlimDto = mockk<CommentSlimDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { commentSlimDto.id } returns 1L
        every { commentSlimDto.owner.email } returns "testuser@example.com"
        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        every { commentRepository.findById(1L) } returns Optional.empty()

        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.editComment(commentSlimDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Comment with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `editComment() should edit the comment successfully`() {
        val commentSlimDto = mockk<CommentSlimDto>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        val originalComment = mockk<Comment>(relaxed = true)
        val updatedComment = mockk<Comment>(relaxed = true)
        val commentDto = mockk<CommentDto>(relaxed = true)

        every { commentSlimDto.id } returns 1L
        every { commentSlimDto.text } returns "Updated text"
        every { commentSlimDto.owner.email } returns "testuser@example.com"
        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        SecurityContextHolder.setContext(securityContext)

        every { commentRepository.findById(eq(1L)) } returns Optional.of(originalComment)
        every { commentRepository.save(originalComment) } returns updatedComment
        every { commentMapper.modelToDto(updatedComment) } returns commentDto

        every { originalComment.id } returns 1L
        every { originalComment.text = "Updated text" } returns Unit
        every { originalComment.isEdited = true } returns Unit

        val response: ResponseEntity<*> = commentService.editComment(commentSlimDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Comment successfully edited!", responseBody.message)
        assertEquals(commentDto, responseBody.data)
    }

    @Test
    fun `likeComment() should fail if the comment is not found`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        every { userRepository.findByEmail("testuser@example.com") } returns Optional.of(mockk(relaxed = true))
        every { commentRepository.findById(1L) } returns Optional.empty()

        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.likeComment(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Comment with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `likeComment() should like the comment successfully`() {
        val user = mockk<User>(relaxed = true)
        val comment = mockk<Comment>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        every { userRepository.findByEmail("testuser@example.com") } returns Optional.of(user)
        every { commentRepository.findById(1L) } returns Optional.of(comment)
        every { comment.likes.add(user) } returns true
        every { commentRepository.save(comment) } returns comment

        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.likeComment(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Comment Liked!", responseBody.message)
        assertEquals("Comment Liked!", responseBody.data)
    }

    @Test
    fun `dislikeComment() should fail if the comment is not found`() {
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        every { userRepository.findByEmail("testuser@example.com") } returns Optional.of(mockk(relaxed = true))
        every { commentRepository.findById(1L) } returns Optional.empty()

        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.dislikeComment(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Comment with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `dislikeComment() should dislike the comment successfully`() {
        val user = mockk<User>(relaxed = true)
        val comment = mockk<Comment>(relaxed = true)
        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)

        every { authentication.name } returns "testuser@example.com"
        every { securityContext.authentication } returns authentication
        every { userRepository.findByEmail("testuser@example.com") } returns Optional.of(user)
        every { commentRepository.findById(1L) } returns Optional.of(comment)
        every { comment.likes.remove(user) } returns true
        every { commentRepository.save(comment) } returns comment

        SecurityContextHolder.setContext(securityContext)

        val response: ResponseEntity<*> = commentService.dislikeComment(1L)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Comment Disliked!", responseBody.message)
        assertEquals("Comment Disliked!", responseBody.data)
    }

    @Test
    fun `getCommentSlim() should fail if the comment is not found`() {
        val commentId = 1L

        every { commentRepository.findById(commentId) } returns Optional.empty()

        val response: ResponseEntity<*> = commentService.getCommentSlim(commentId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Comment with this id 1!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `getCommentSlim() should return the comment successfully`() {
        val commentId = 1L
        val comment = mockk<Comment>(relaxed = true)
        val commentSlimDto = mockk<CommentSlimDto>(relaxed = true)

        every { commentRepository.findById(commentId) } returns Optional.of(comment)
        every { commentMapper.modelToSlimDto(comment) } returns commentSlimDto

        val response: ResponseEntity<*> = commentService.getCommentSlim(commentId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("", responseBody.message)
        assertEquals(commentSlimDto, responseBody.data)
    }
}