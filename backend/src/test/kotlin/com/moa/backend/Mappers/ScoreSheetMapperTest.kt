package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.*
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.repository.UserRepository
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
class ScoreSheetMapperTest {
    private lateinit var scoreSheetMapper: ScoreSheetMapper
    private lateinit var ideaMapper: IdeaMapper
    private lateinit var userMapper: UserMapper
    private lateinit var ideaBoxMapper: IdeaBoxMapper
    private lateinit var scoreItemMapper: ScoreItemMapper
    private lateinit var scoreSheetRepository: ScoreSheetRepository
    private lateinit var testUtility: TestUtility

    @BeforeEach
    fun setup() {
        ideaBoxMapper = mockk()
        userMapper = mockk()
        scoreItemMapper = mockk()
        scoreSheetRepository = mockk()
        ideaMapper = mockk()
        testUtility = TestUtility()

        scoreSheetMapper = ScoreSheetMapper().apply {
            this.ideaBoxMapper = this@ScoreSheetMapperTest.ideaBoxMapper
            this.ideaMapper = this@ScoreSheetMapperTest.ideaMapper
            this.userMapper = this@ScoreSheetMapperTest.userMapper
            this.scoreItemMapper = this@ScoreSheetMapperTest.scoreItemMapper
            this.scoreSheetRepository = this@ScoreSheetMapperTest.scoreSheetRepository
        }
    }

    @Test
    fun `should return ScoreSheetDto from ScoreSheet (modelToDto)`() {
        val scoreSheet = testUtility.createMockScoreSheet()
        val expectedScoreSheetDto = testUtility.createMockScoreSheetDto()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()
        val expectedIdeaSlimDto = testUtility.createMockSlimIdeaDto()
        val expectedIdeaBoxSlimDto = testUtility.createMockSlimIdeaBoxDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto
        every { ideaMapper.modelToSlimDto(any()) } returns expectedIdeaSlimDto
        every { ideaBoxMapper.modelToSlimDto(any()) } returns expectedIdeaBoxSlimDto

        val scoreSheetDto = scoreSheetMapper.modelToDto(scoreSheet)

        Assertions.assertEquals(expectedScoreSheetDto, scoreSheetDto)
    }

    @Test
    fun `should return ScoreSheetSlimDto from ScoreSheet (modelToSlimDto)`() {
        val scoreSheet = testUtility.createMockScoreSheet()
        val expectedScoreItemSlimDto = testUtility.createMockSlimScoreSheetDto()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()
        val expectedIdeaSlimDto = testUtility.createMockSlimIdeaDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto
        every { ideaMapper.modelToSlimDto(any()) } returns expectedIdeaSlimDto

        val scoreSheetSlimDto = scoreSheetMapper.modelToSlimDto(scoreSheet)

        Assertions.assertEquals(expectedScoreItemSlimDto, scoreSheetSlimDto)
    }

    @Test
    fun `should return ScoreSheet from ScoreSheetDto (dtoToModel)`() {
        val scoreSheetDto = testUtility.createMockScoreSheetDto()
        val expectedScoreSheet = testUtility.createMockScoreSheet()


        every { scoreSheetRepository.findById(any()) } returns Optional.of(expectedScoreSheet)


        val scoreSheet = scoreSheetMapper.dtoToModel(scoreSheetDto)

        Assertions.assertEquals(expectedScoreSheet, scoreSheet)
    }

    @Test
    fun `should return ScoreSheet from ScoreSheetDto (id=0L) (dtoToModel)`() {
        val scoreSheetDto = testUtility.createMockScoreSheetDto()
        scoreSheetDto.id = 0L
        val expectedScoreSheet = testUtility.createMockScoreSheet()
        expectedScoreSheet.id = 0L
        val expectedIdea = testUtility.createMockIdea()
        val expectedUser = testUtility.createMockUser()
        val expectedIdeaBox = testUtility.createMockIdeaBox()


        every { scoreSheetRepository.findById(any()) } returns Optional.of(expectedScoreSheet)
        every { ideaMapper.slimDtoToModel(any()) } returns expectedIdea
        every { userMapper.slimDtoToModel(any()) } returns expectedUser
        every { ideaBoxMapper.slimDtoToModel(any()) } returns expectedIdeaBox

        val scoreSheet = scoreSheetMapper.dtoToModel(scoreSheetDto)

        Assertions.assertEquals(expectedScoreSheet.id, scoreSheet.id)
    }

    @Test
    fun `should return ScoreSheet from ScoreSheetSlimDto (slimDtoToModel)`() {
        val scoreSheetSlimDto = testUtility.createMockSlimScoreSheetDto()
        val expectedScoreSheet = testUtility.createMockScoreSheet()


        every { scoreSheetRepository.findById(any()) } returns Optional.of(expectedScoreSheet)


        val scoreSheet = scoreSheetMapper.slimDtoToModel(scoreSheetSlimDto)

        Assertions.assertEquals(expectedScoreSheet, scoreSheet)
    }

}