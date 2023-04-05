package com.moa.backend.controller

import com.moa.backend.model.IdeaBox
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.service.IdeaBoxService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class IdeaBoxController(private val ideaBoxRepository: IdeaBoxRepository) {

    @Autowired
    lateinit var ideaBoxService: IdeaBoxService

    @GetMapping("/idea-box")
    fun getIdeaBoxes(): List<IdeaBox> {
        return ideaBoxService.getIdeaBoxes()
    }

    @GetMapping("/idea-box/{id}")
    fun getIdeaBox(@PathVariable id: Long): IdeaBox {
        return  ideaBoxService.getIdeaBox(id)
    }

    @PostMapping("/idea-box")
    fun createIdeaBox(@RequestBody box: IdeaBox): IdeaBox {
        return ideaBoxService.createIdeaBox(box)
    }

    @PutMapping("/idea-box/{id}")
    fun updateIdeaBox(@PathVariable id: Long, @RequestBody box: IdeaBox): IdeaBox {
        return ideaBoxService.updateIdeaBox(id, box)
    }

    @DeleteMapping("/idea-box/{id}")
    fun deleteIdeaBox(@PathVariable id: Long): Any {
        return ideaBoxService.deleteIdeaBox(id)
    }
}