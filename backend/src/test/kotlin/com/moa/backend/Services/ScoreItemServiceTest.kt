package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.ScoreItem
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.repository.*
import com.moa.backend.service.ScoreItemService
import com.moa.backend.service.UserService
import com.moa.backend.utility.Functions
import com.moa.backend.utility.WebResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.*

@SpringBootTest
@ExtendWith(MockKExtension::class)
class ScoreItemServiceTest {

    @Autowired
    private lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    private lateinit var functions: Functions

    @MockkBean
    lateinit var ideaRepository: IdeaRepository

    @MockkBean
    lateinit var ideaBoxRepository: IdeaBoxRepository

    @MockkBean
    lateinit var userRepository: UserRepository

    @MockkBean
    lateinit var ideaMapper: IdeaMapper

    @MockkBean
    lateinit var scoreItemMapper: ScoreItemMapper

    @MockkBean
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @MockkBean
    lateinit var scoreItemRepository: ScoreItemRepository

    @MockkBean
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var scoreItemService: ScoreItemService

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `CreateScoreItem(scoreItems, id) should add score items successfully`() {
        val id = 1L
        val scoreItemsDto = mutableListOf<ScoreItemDto>(
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        val scoreItemModels = scoreItemsDto.map { mockk<ScoreItem>(relaxed = true) }

        scoreItemsDto.forEachIndexed { index, dto ->
            every { scoreItemMapper.dtoToModel(dto) } returns scoreItemModels[index]
        }

        scoreItemModels.forEach { model ->
            every { scoreItemRepository.save(model) } returns model
        }

        val response: ResponseEntity<*> = scoreItemService.CreateScoreItem(scoreItemsDto, id)

        assertEquals(HttpStatus.OK, response.statusCode)
        val webResponse = response.body as WebResponse<String>
        assertEquals(HttpStatus.OK.value(), webResponse.code)
        assertEquals("Item Added Successfully", webResponse.message)
        assertEquals("ok", webResponse.data)

        scoreItemsDto.forEach { dto ->
            verify { scoreItemMapper.dtoToModel(dto) }
        }
        scoreItemModels.forEach { model ->
            verify { scoreItemRepository.save(model) }
        }
    }

}