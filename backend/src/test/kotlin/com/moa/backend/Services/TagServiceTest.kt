package com.moa.backend.Services

import TestUtility
import com.moa.backend.mapper.TagMapper
import com.moa.backend.model.Tag
import com.moa.backend.model.dto.TagDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.repository.TagRepository
import com.moa.backend.service.TagService
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
class TagServiceTest {

    @MockkBean
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var tagService: TagService

    @MockkBean
    lateinit var tagMapper: TagMapper

    private lateinit var testUtil: TestUtility

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        testUtil = TestUtility()
    }

    @Test
    fun `getTag() should return not found if tag does not exist`() {
        val tagId = 1L

        val authentication = mockk<Authentication>(relaxed = true)
        val securityContext = mockk<SecurityContext>(relaxed = true)
        SecurityContextHolder.setContext(securityContext)
        every { securityContext.authentication } returns authentication

        every { tagRepository.findById(tagId) } returns Optional.empty()

        val response: ResponseEntity<*> = tagService.getTag(tagId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Tag with this id $tagId!", responseBody.message)
        assertNull(responseBody.data)

        SecurityContextHolder.clearContext()
    }

    @Test
    fun `getTag() should return tag if it exists`() {
        val tagId = 1L
        val tag = mockk<Tag>(relaxed = true)
        val tagDto = mockk<TagDto>(relaxed = true)

        every { tagRepository.findById(any()) } returns Optional.of(tag)
        every { tagMapper.modelToDto(tag) } returns tagDto

        val response: ResponseEntity<*> = tagService.getTag(tagId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals(tagDto, responseBody.data)
        assertEquals("", responseBody.message)
    }

    @Test
    fun `getTagSlim() should return not found if tag does not exist`() {
        val tagId = 1L

        every { tagRepository.findById(tagId) } returns Optional.empty()

        val response: ResponseEntity<*> = tagService.getTagSlim(tagId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.code)
        assertEquals("Cannot find Tag with this id $tagId!", responseBody.message)
        assertNull(responseBody.data)
    }

    @Test
    fun `getTagSlim() should return tag slim dto if tag exists`() {
        val tagId = 1L
        val tag = mockk<Tag>(relaxed = true)
        val tagSlimDto = mockk<TagSlimDto>(relaxed = true)

        every { tagRepository.findById(tagId) } returns Optional.of(tag)
        every { tagMapper.modelToSlimDto(tag) } returns tagSlimDto

        val response: ResponseEntity<*> = tagService.getTagSlim(tagId)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals(tagSlimDto, responseBody.data)
        assertEquals("", responseBody.message)
    }

    @Test
    fun `getTags() should return list of tag slim dtos`() {
        val tags = listOf(mockk<Tag>(relaxed = true), mockk<Tag>(relaxed = true))
        val tagSlimDto1 = mockk<TagSlimDto>(relaxed = true)
        val tagSlimDto2 = mockk<TagSlimDto>(relaxed = true)

        every { tagRepository.findAll() } returns tags
        every { tagMapper.modelToSlimDto(tags[0]) } returns tagSlimDto1
        every { tagMapper.modelToSlimDto(tags[1]) } returns tagSlimDto2

        val response: ResponseEntity<*> = tagService.getTags()

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals(listOf(tagSlimDto1, tagSlimDto2), responseBody.data)
        assertEquals("", responseBody.message)
    }

    @Test
    fun `createTag() should return created tag dto with success message`() {
        val tagDto = mockk<TagDto>(relaxed = true)
        val tag = mockk<Tag>(relaxed = true)
        val savedTag = mockk<Tag>(relaxed = true)
        val createdTagDto = mockk<TagDto>(relaxed = true)

        every { tagRepository.saveAndFlush(any()) } returns savedTag
        every { tagMapper.dtoToModel(tagDto) } returns tag
        every { tagMapper.modelToDto(savedTag) } returns createdTagDto

        val response: ResponseEntity<*> = tagService.createTag(tagDto)

        val responseBody = response.body as WebResponse<*>
        assertEquals(HttpStatus.OK.value(), responseBody.code)
        assertEquals("Tag Successfully Created!", responseBody.message)
        assertEquals(createdTagDto, responseBody.data)
    }

}