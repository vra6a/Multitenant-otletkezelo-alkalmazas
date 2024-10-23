package com.moa.backend.service

import com.moa.backend.mapper.TagMapper
import com.moa.backend.model.dto.TagDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.repository.TagRepository
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TagService {

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var tagMapper: TagMapper

    fun getTag(id: Long): ResponseEntity<*> {
        val tag = tagRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Tag with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<TagDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = tagMapper.modelToDto(tag)
            )
        )
    }

    fun getTagSlim(id: Long): ResponseEntity<*> {
        val tag = tagRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Tag with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<TagSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = tagMapper.modelToSlimDto(tag)
            )
        )
    }

    fun getTags(): ResponseEntity<*> {
        val tags = tagRepository.findAll()
        val response: MutableList<TagSlimDto> = emptyList<TagSlimDto>().toMutableList()

        for (tag in tags) {
            tag.let {
                response.add(tagMapper.modelToSlimDto(tag))
            }
        }
        return ResponseEntity.ok(
            WebResponse<MutableList<TagSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createTag(tag: TagDto): ResponseEntity<*> {
        return ResponseEntity.ok(
            WebResponse<TagDto>(
                code = HttpStatus.OK.value(),
                message = "Tag Successfully Created!",
                data = tagMapper.modelToDto(
                    tagRepository.saveAndFlush(
                        tagMapper.dtoToModel(tag)
                    )
                )
            )
        )
    }


}