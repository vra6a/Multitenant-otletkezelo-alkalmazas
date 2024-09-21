package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.repository.ScoreItemRepository
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
class ScoreItemMapperTest {

    private lateinit var scoreItemMapper: ScoreItemMapper
    private lateinit var ideaMapper: IdeaMapper
    private lateinit var scoreSheetMapper: ScoreSheetMapper
    private lateinit var scoreItemRepository: ScoreItemRepository
    private lateinit var testUtility: TestUtility

    @BeforeEach
    fun setup() {
        ideaMapper = mockk()
        scoreSheetMapper = mockk()
        scoreItemRepository = mockk()

        testUtility = TestUtility()

        scoreItemMapper = ScoreItemMapper().apply {
            this.ideaMapper = this@ScoreItemMapperTest.ideaMapper
            this.scoreSheetMapper = this@ScoreItemMapperTest.scoreSheetMapper
            this.scoreItemRepository = this@ScoreItemMapperTest.scoreItemRepository
        }
    }

    @Test
    fun `should return ScoreItemDto from ScoreItem (modelToDto)`() {
        val scoreItem = testUtility.createMockScoreItem()
        val expectedScoreItemDto = testUtility.createMockScoreItemDto()
        val expectedScoreSheetSlimDto = testUtility.createMockSlimScoreSheetDto()

        every { scoreSheetMapper.modelToSlimDto(any()) } returns expectedScoreSheetSlimDto

        val scoreItemDto = scoreItemMapper.modelToDto(scoreItem)

        Assertions.assertEquals(expectedScoreItemDto, scoreItemDto)
    }

    @Test
    fun `should return ScoreItemSlimDto from ScoreItem (modelToSlimDto)`() {
        val scoreItem = testUtility.createMockScoreItem()
        val expectedScoreItemSlimDto = testUtility.createMockSlimScoreItemDto()
        val expectedScoreSheetSlimDto = testUtility.createMockSlimScoreSheetDto()

        every { scoreSheetMapper.modelToSlimDto(any()) } returns expectedScoreSheetSlimDto

        val scoreItemDto = scoreItemMapper.modelToSlimDto(scoreItem)

        Assertions.assertEquals(expectedScoreItemSlimDto, scoreItemDto)
    }

    @Test
    fun `should return ScoreItem from ScoreItemDto (dtoToModel)`() {
        val scoreItemDto = testUtility.createMockScoreItemDto()
        val expectedScoreItem = testUtility.createMockScoreItem()

        every { scoreItemRepository.findById(any()) } returns Optional.of(expectedScoreItem)

        val scoreItem = scoreItemMapper.dtoToModel(scoreItemDto)

        Assertions.assertEquals(expectedScoreItem, scoreItem)
    }

    @Test
    fun `should return ScoreItem from ScoreItemDto (id=0L) (dtoToModel)`() {
        val scoreItemDto = testUtility.createMockScoreItemDto()
        scoreItemDto.id = 0L
        val expectedScoreItem = testUtility.createMockScoreItem()
        expectedScoreItem.id = 0L
        val expectedScoreSheet = testUtility.createMockScoreSheet()

        every { scoreItemRepository.findById(any()) } returns Optional.of(expectedScoreItem)
        every { scoreSheetMapper.slimDtoToModel(any()) } returns expectedScoreSheet

        val scoreItem = scoreItemMapper.dtoToModel(scoreItemDto)

        Assertions.assertEquals(expectedScoreItem.id, scoreItem.id)
    }

    @Test
    fun `should return ScoreItem from ScoreItemSlimDto (slimDtoToModel)`() {
        val scoreItemSlimDto = testUtility.createMockSlimScoreItemDto()
        val expectedScoreItem = testUtility.createMockScoreItem()

        every { scoreItemRepository.findById(any()) } returns Optional.of(expectedScoreItem)

        val scoreItem = scoreItemMapper.slimDtoToModel(scoreItemSlimDto)

        Assertions.assertEquals(expectedScoreItem, scoreItem)
    }

}