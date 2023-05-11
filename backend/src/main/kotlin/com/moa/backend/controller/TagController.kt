package com.moa.backend.controller

import com.moa.backend.model.dto.TagDto
import com.moa.backend.repository.TagRepository
import com.moa.backend.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class TagController(private val tagRepository: TagRepository) {

    @Autowired
    lateinit var tagService: TagService

    @GetMapping("/tags")
    fun getTags(): ResponseEntity<*> {
        return tagService.getTags()
    }

    @GetMapping("/tag/{id}")
    fun getTag(@PathVariable id: Long): ResponseEntity<*> {
        return tagService.getTag(id)
    }

    @GetMapping("/tag/slim/{id}")
    fun getTagSlim(@PathVariable id: Long): ResponseEntity<*> {
        return tagService.getTagSlim(id)
    }

    @PostMapping("/tag")
    fun createTag(@RequestBody tag: TagDto): ResponseEntity<*> {
        return tagService.createTag(tag)
    }
}