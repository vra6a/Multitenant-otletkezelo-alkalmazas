package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.*
import com.moa.backend.repository.IdeaBoxRepository
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
class IdeaBoxMapperTest {

    private lateinit var userMapper: UserMapper
    private lateinit var ideaMapper: IdeaMapper
    private lateinit var ideaBoxMapper: IdeaBoxMapper
    private lateinit var scoreSheetMapper: ScoreSheetMapper
    private lateinit var ideaBoxRepository: IdeaBoxRepository
    private lateinit var testUtility: TestUtility

    @BeforeEach
    fun setup() {
        userMapper = mockk()
        ideaMapper = mockk()
        scoreSheetMapper = mockk()
        ideaBoxRepository = mockk()

        testUtility = TestUtility()

        ideaBoxMapper = IdeaBoxMapper().apply {
            this.userMapper = this@IdeaBoxMapperTest.userMapper
            this.ideaMapper = this@IdeaBoxMapperTest.ideaMapper
            this.scoreSheetMapper = this@IdeaBoxMapperTest.scoreSheetMapper
            this.ideaBoxRepository = this@IdeaBoxMapperTest.ideaBoxRepository
        }
    }

    @Test
    fun `should return IdeaBoxDto from IdeaBox (modelToDto)`() {
        val ideaBox = testUtility.createMockIdeaBox()
        val expectedIdeaBoxDto = testUtility.createMockIdeaBoxDto()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()

        every { userMapper.modelToSlimDto(any()) } returns expectedUserSlimDto

        val ideaBoxDto = ideaBoxMapper.modelToDto(ideaBox)

        Assertions.assertEquals(expectedIdeaBoxDto, ideaBoxDto)
    }

    @Test
    fun `should return IdeaBoxSlimDto from IdeaBox (modelToSlimDto)`() {
        val ideaBox = testUtility.createMockIdeaBox()
        val expectedIdeaBoxSlimDto = testUtility.createMockSlimIdeaBoxDto()

        val ideaBoxSlimDto = ideaBoxMapper.modelToSlimDto(ideaBox)

        Assertions.assertEquals(expectedIdeaBoxSlimDto, ideaBoxSlimDto)
    }

    @Test
    fun `should return IdeaBox from IdeaBoxDto (dtoToModel)`() {
        val ideaBoxDto = testUtility.createMockIdeaBoxDto()
        val expectedIdeaBox = testUtility.createMockIdeaBox()

        every { ideaBoxRepository.findById(any()) } returns Optional.of(expectedIdeaBox)

        val ideaBox = ideaBoxMapper.dtoToModel(ideaBoxDto)

        Assertions.assertEquals(expectedIdeaBox, ideaBox)
    }

    @Test
    fun `should return IdeaBox from IdeaBoxDto (id=0L) (dtoToModel)`() {
        val ideaBoxDto = testUtility.createMockIdeaBoxDto()
        ideaBoxDto.id = 0L
        val expectedIdeaBox = testUtility.createMockIdeaBox()
        expectedIdeaBox.id = 0L
        val expectedUser = testUtility.createMockUser()

        every { ideaBoxRepository.findById(any()) } returns Optional.of(expectedIdeaBox)
        every { userMapper.slimDtoToModel(any()) } returns expectedUser

        val ideaBox = ideaBoxMapper.dtoToModel(ideaBoxDto)

        Assertions.assertEquals(expectedIdeaBox.id, ideaBox.id)
    }

    @Test
    fun `should return IdeaBox from IdeaBoxSlimDto (slimDtoToModel)`() {
        val ideaBoxSlimDto = testUtility.createMockSlimIdeaBoxDto()
        val expectedIdeaBox = testUtility.createMockIdeaBox()

        every { ideaBoxRepository.findById(any()) } returns Optional.of(expectedIdeaBox)

        val ideaBox = ideaBoxMapper.slimDtoToModel(ideaBoxSlimDto)

        Assertions.assertEquals(expectedIdeaBox, ideaBox)
    }
}