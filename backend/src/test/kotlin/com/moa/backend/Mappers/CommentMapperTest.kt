package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.*
import com.moa.backend.repository.CommentRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CommentMapperTest {

    private lateinit var ideaMapper: IdeaMapper
    private lateinit var userMapper: UserMapper
    private lateinit var commentMapper: CommentMapper
    private lateinit var testUtility: TestUtility
    private lateinit var commentRepository: CommentRepository

    @BeforeEach
    fun setup() {
        userMapper = mockk()
        ideaMapper = mockk()
        commentRepository = mockk()

        testUtility = TestUtility()

        commentMapper = CommentMapper().apply {
            this.userMapper = this@CommentMapperTest.userMapper
            this.ideaMapper = this@CommentMapperTest.ideaMapper
            this.commentRepository = this@CommentMapperTest.commentRepository
        }
    }

    @Test
    fun `should return CommentDto from Comment (modelToDto)`() {
        val comment = testUtility.createMockComment()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()
        val expectedIdeaSlimDto = testUtility.createMockSlimIdeaDto()
        val expectedCommentDto = testUtility.createMockCommentDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto
        every { ideaMapper.modelToSlimDto(any()) } returns expectedIdeaSlimDto

        val commentDto = commentMapper.modelToDto(comment)

        Assertions.assertEquals(expectedCommentDto, commentDto)
    }

    @Test
    fun `should return CommentSlimDto from Comment (modelToSlimDto)`() {
        val comment = testUtility.createMockComment()
        val expectedCommentSlimDto = testUtility.createMockSlimCommentDto()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()
        val expectedIdeaSlimDto = testUtility.createMockSlimIdeaDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto
        every { ideaMapper.modelToSlimDto(any()) } returns expectedIdeaSlimDto

        val commentSlimDto = commentMapper.modelToSlimDto(comment)

        Assertions.assertEquals(expectedCommentSlimDto, commentSlimDto)
    }

    @Test
    fun `should return Comment from CommentDto (dtoToModel)`() {
        val commentDto = testUtility.createMockCommentDto()
        val expectedComment = testUtility.createMockComment()

        every { commentRepository.findById(any()) } returns Optional.of(expectedComment)

        val comment = commentMapper.dtoToModel(commentDto)

        Assertions.assertEquals(expectedComment, comment)
    }

    @Test
    fun `should return Comment from CommentDto (id=0L) (dtoToModel)`() {
        val commentDto = testUtility.createMockCommentDto()
        commentDto.id = 0L
        val expectedComment = testUtility.createMockComment()
        expectedComment.id = 0L
        val expectedUser = testUtility.createMockUser()
        val expectedIdea = testUtility.createMockIdea()

        every { commentRepository.findById(any()) } returns Optional.of(expectedComment)
        every { userMapper.slimDtoToModel(any()) } returns expectedUser
        every { ideaMapper.slimDtoToModel(any()) } returns expectedIdea

        val comment = commentMapper.dtoToModel(commentDto)

        Assertions.assertEquals(expectedComment.id, comment.id)
    }

    @Test
    fun `should return Comment from CommentSlimDto (slimDtoToModel)`() {
        val commentSlimDto = testUtility.createMockSlimCommentDto()
        val expectedComment = testUtility.createMockComment()

        every { commentRepository.findById(any()) } returns Optional.of(expectedComment)

        val comment = commentMapper.slimDtoToModel(commentSlimDto)

        Assertions.assertEquals(expectedComment, comment)
    }
}