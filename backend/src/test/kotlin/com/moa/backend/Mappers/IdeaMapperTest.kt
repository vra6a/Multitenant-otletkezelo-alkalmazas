package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.*
import com.moa.backend.repository.IdeaRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.util.Optional

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class IdeaMapperTest {

    private lateinit var ideaMapper: IdeaMapper
    private lateinit var userMapper: UserMapper
    private lateinit var tagMapper: TagMapper
    private lateinit var commentMapper: CommentMapper
    private lateinit var ideaBoxMapper: IdeaBoxMapper
    private lateinit var scoreSheetMapper: ScoreSheetMapper

    private lateinit var testUtility: TestUtility
    private lateinit var ideaRepository: IdeaRepository

    @BeforeEach
    fun setup() {
        userMapper = mockk()
        tagMapper = mockk()
        commentMapper = mockk()
        ideaBoxMapper = mockk()
        scoreSheetMapper = mockk()
        ideaRepository = mockk()

        testUtility = TestUtility()

        ideaMapper = IdeaMapper().apply {
            this.userMapper = this@IdeaMapperTest.userMapper
            this.tagMapper = this@IdeaMapperTest.tagMapper
            this.commentMapper = this@IdeaMapperTest.commentMapper
            this.ideaBoxMapper = this@IdeaMapperTest.ideaBoxMapper
            this.scoreSheetMapper = this@IdeaMapperTest.scoreSheetMapper
            this.ideaRepository = this@IdeaMapperTest.ideaRepository
        }
    }

    @Test
    fun `should return IdeaDto from Idea (modelToDto)`() {
        val idea = testUtility.createMockIdea()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()
        val expectedTagSlimDto = testUtility.createMockSlimTagDto()
        val expectedCommentSlimDto = testUtility.createMockSlimCommentDto()
        val expectedIdeaBoxSlimDto = testUtility.createMockSlimIdeaBoxDto()
        val expectedScoreSheetSlimDto = testUtility.createMockSlimScoreSheetDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto
        every { tagMapper.modelToSlimDto(any()) } returns expectedTagSlimDto
        every { commentMapper.modelToSlimDto(any()) } returns expectedCommentSlimDto
        every { ideaBoxMapper.modelToSlimDto(any()) } returns expectedIdeaBoxSlimDto
        every { scoreSheetMapper.modelToSlimDto(any()) } returns expectedScoreSheetSlimDto

        val ideaDto = ideaMapper.modelToDto(idea)

        Assertions.assertEquals(idea.id, ideaDto.id)
        Assertions.assertEquals(idea.title, ideaDto.title)
        Assertions.assertEquals(idea.description, ideaDto.description)
        Assertions.assertEquals(idea.status, ideaDto.status)
        Assertions.assertEquals(expectedUserSlimDto, ideaDto.owner)
        Assertions.assertEquals(expectedIdeaBoxSlimDto, ideaDto.ideaBox)

    }

    @Test
    fun `should return IdeaSlimDto from Idea (modelToSlimDto)`() {
        val idea = testUtility.createMockIdea()
        val slimIdeaDto = ideaMapper.modelToSlimDto(idea)

        Assertions.assertEquals(idea.id, slimIdeaDto.id)
        Assertions.assertEquals(idea.title, slimIdeaDto.title)
        Assertions.assertEquals(idea.status, slimIdeaDto.status)
    }

    @Test
    fun `should return Idea from IdeaDto (dtoToModel)`() {
        val ideaDto = testUtility.createMockIdeaDto()
        val expectedUser = testUtility.createMockUser()
        val expectedTag = testUtility.createMockTag()
        val expectedComment = testUtility.createMockComment()
        val expectedIdeaBox = testUtility.createMockIdeaBox()
        val expectedScoreSheet = testUtility.createMockScoreSheet()
        val expectedIdea = testUtility.createMockIdea()

        every { userMapper.dtoToModel(any()) } returns expectedUser
        every { tagMapper.dtoToModel(any()) } returns expectedTag
        every { commentMapper.dtoToModel(any()) } returns expectedComment
        every { ideaBoxMapper.dtoToModel(any()) } returns expectedIdeaBox
        every { scoreSheetMapper.dtoToModel(any()) } returns expectedScoreSheet
        every { ideaRepository.findById(ideaDto.id) } returns Optional.of(expectedIdea)

        val idea = ideaMapper.dtoToModel(ideaDto)

        Assertions.assertEquals(ideaDto.id, idea.id)
        Assertions.assertEquals(ideaDto.title, idea.title)
        Assertions.assertEquals(ideaDto.description, idea.description)
        Assertions.assertEquals(ideaDto.status, idea.status)
        Assertions.assertEquals(expectedUser, idea.owner)
        Assertions.assertEquals(expectedIdeaBox, idea.ideaBox)
    }

    @Test
    fun `should return Idea from IdeaDto (id=0L) ((dtoToModel)`() {
        val ideaDto = testUtility.createMockIdeaDto()
        ideaDto.id = 0L
        val expectedUser = testUtility.createMockUser()
        expectedUser.id = 0L
        val expectedTag = testUtility.createMockTag()
        val expectedComment = testUtility.createMockComment()
        val expectedIdeaBox = testUtility.createMockIdeaBox()
        val expectedScoreSheet = testUtility.createMockScoreSheet()
        val expectedIdea = testUtility.createMockIdea()

        every { userMapper.dtoToModel(any()) } returns expectedUser
        every { tagMapper.dtoToModel(any()) } returns expectedTag
        every { commentMapper.dtoToModel(any()) } returns expectedComment
        every { ideaBoxMapper.dtoToModel(any()) } returns expectedIdeaBox
        every { scoreSheetMapper.dtoToModel(any()) } returns expectedScoreSheet
        every { ideaRepository.findById(ideaDto.id) } returns Optional.of(expectedIdea)
        every { userMapper.slimDtoToModel(any()) } returns expectedUser
        every { ideaBoxMapper.slimDtoToModel(any()) } returns expectedIdeaBox

        val idea = ideaMapper.dtoToModel(ideaDto)

        Assertions.assertEquals(ideaDto.id, idea.id)
    }

    @Test
    fun `should return Idea from IdeaSlimDto (slimDtoToModel)`() {
        val ideaSlimDto = testUtility.createMockIdeaDto()
        val expectedUser = testUtility.createMockUser()
        val expectedTag = testUtility.createMockTag()
        val expectedComment = testUtility.createMockComment()
        val expectedIdeaBox = testUtility.createMockIdeaBox()
        val expectedScoreSheet = testUtility.createMockScoreSheet()
        val expectedIdea = testUtility.createMockIdea()

        every { userMapper.dtoToModel(any()) } returns expectedUser
        every { tagMapper.dtoToModel(any()) } returns expectedTag
        every { commentMapper.dtoToModel(any()) } returns expectedComment
        every { ideaBoxMapper.dtoToModel(any()) } returns expectedIdeaBox
        every { scoreSheetMapper.dtoToModel(any()) } returns expectedScoreSheet
        every { ideaRepository.findById(ideaSlimDto.id) } returns Optional.of(expectedIdea)

        val idea = ideaMapper.dtoToModel(ideaSlimDto)

        Assertions.assertEquals(expectedIdea.id, idea.id)
        Assertions.assertEquals(expectedIdea.title, idea.title)
        Assertions.assertEquals(expectedIdea.description, idea.description)
        Assertions.assertEquals(expectedIdea.status, idea.status)
        Assertions.assertEquals(expectedUser, idea.owner)
        Assertions.assertEquals(expectedIdeaBox, idea.ideaBox)
    }
}



