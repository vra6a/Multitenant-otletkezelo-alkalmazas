package com.moa.backend.controller

import com.moa.backend.model.Idea
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.service.IdeaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class IdeaController(private val ideaRepository: IdeaRepository) {

    @Autowired
    lateinit var ideaService: IdeaService

    @GetMapping("/ideas")
    fun getIdeas(): List<Idea> {
        return ideaService.getIdeas()
    }

    @GetMapping("/idea/{id}")
    fun getIdea(@PathVariable id: Long): Idea {
        return ideaService.getIdea(id)
    }

    @PostMapping("/idea")
    fun createIdea(@RequestBody idea: Idea): Idea {
        return ideaService.createIdea(idea)
    }

    @PutMapping("/idea/{id}")
    fun updateIdea(@PathVariable id: Long, @RequestBody idea: Idea): Idea {
        return  ideaService.updateIdea(id, idea)
    }

    @DeleteMapping("/idea/{id}")
    fun deleteIdea(@PathVariable id: Long): Any {
        return ideaService.deleteIdea(id)
    }
}