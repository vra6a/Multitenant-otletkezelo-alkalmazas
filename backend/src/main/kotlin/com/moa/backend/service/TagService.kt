package com.moa.backend.service

import com.moa.backend.mapper.TagMapper
import com.moa.backend.model.Tag
import com.moa.backend.model.dto.TagDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.TagRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class TagService {

    @Autowired
    lateinit var tagRepository: TagRepository

    @Autowired
    lateinit var tagMapper: TagMapper

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun getTag(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val tag = currentEntityManager.find(Tag::class.java, id)
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val tag = currentEntityManager.find(Tag::class.java, id)
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val tags = currentEntityManager.createQuery("SELECT t FROM Tag t", Tag::class.java).resultList
        val response: MutableList<TagSlimDto> = tags.map { tagMapper.modelToSlimDto(it) }.toMutableList()

        return ResponseEntity.ok(
            WebResponse<MutableList<TagSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createTag(tag: TagDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val savedTag = currentEntityManager.merge(tagMapper.dtoToModel(tag))

        return ResponseEntity.ok(
            WebResponse<TagDto>(
                code = HttpStatus.OK.value(),
                message = "Tag Successfully Created!",
                data = tagMapper.modelToDto(savedTag)
            )
        )
    }
}