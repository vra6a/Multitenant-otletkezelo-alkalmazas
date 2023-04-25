package com.moa.backend.controller

import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.service.IdeaBoxService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
@EnableMethodSecurity
class IdeaBoxController(private val ideaBoxRepository: IdeaBoxRepository) {

    @Autowired
    lateinit var ideaBoxService: IdeaBoxService

    @GetMapping("/idea-box")
    @Secured("ADMIN")
    fun getIdeaBoxes(
        @RequestParam("s", defaultValue = "") s: String,
        @RequestParam("sort", defaultValue = "") sort: String,
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("items", defaultValue = "12") items: Int
    ): ResponseEntity<MutableList<IdeaBoxSlimDto>> {
        var direction = Sort.unsorted()
        when(sort) {
            "newest" -> direction = Sort.by(Sort.Direction.DESC, "startDate")
            "oldest" -> direction = Sort.by(Sort.Direction.ASC, "startDate")
            "closing" -> direction = Sort.by(Sort.Direction.ASC, "endDate")
        }

        return ideaBoxService.getIdeaBoxes(s, PageRequest.of(page-1, items, direction))
    }

    @GetMapping("/idea-box-count")
    fun getIdeaBoxCount(): Int {
        return ideaBoxService.getIdeaBoxCount()
    }

    @GetMapping("/idea-box/{id}")
    fun getIdeaBox(@PathVariable id: Long): ResponseEntity<*> {
        return  ideaBoxService.getIdeaBox(id)
    }

    @PostMapping("/idea-box")
    fun createIdeaBox(@RequestBody box: IdeaBoxDto): ResponseEntity<*> {
        return ideaBoxService.createIdeaBox(box)
    }

    @PutMapping("/idea-box/{id}")
    fun updateIdeaBox(@PathVariable id: Long, @RequestBody box: IdeaBoxDto): ResponseEntity<*> {
        return ideaBoxService.updateIdeaBox(id, box)
    }

    @DeleteMapping("/idea-box/{id}")
    fun deleteIdeaBox(@PathVariable id: Long): Any {
        return ideaBoxService.deleteIdeaBox(id)
    }
}