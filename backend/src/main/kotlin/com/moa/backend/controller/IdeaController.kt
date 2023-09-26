package com.moa.backend.controller

import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.dto.ScoreDto
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.service.IdeaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class IdeaController(private val ideaRepository: IdeaRepository) {

    @Autowired
    lateinit var ideaService: IdeaService

    @GetMapping("/ideas")
    fun getIdeas(): ResponseEntity<*> {
        return ideaService.getIdeas()
    }

    @GetMapping("/ideas/reviewed")
    fun getReviewedIdeas(): ResponseEntity<*> {
        return ideaService.getReviewedIdeas()
    }

    @GetMapping("/idea/{id}")
    fun getIdea(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.getIdea(id)
    }

    @GetMapping("/idea/{id}/juries")
    fun getDefaultJuries(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.getDefaultJuries(id)
    }

    @GetMapping("/idea/slim/{id}")
    fun getIdeaSlim(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.getIdeaSlim(id)
    }

    @PostMapping("/idea")
    fun createIdea(@RequestBody idea: IdeaDto): ResponseEntity<*> {
        return ideaService.createIdea(idea)
    }

    @PutMapping("/idea/{id}")
    fun updateIdea(@PathVariable id: Long, @RequestBody idea: IdeaDto): ResponseEntity<*> {
        return  ideaService.updateIdea(id, idea)
    }

    @DeleteMapping("/idea/{id}")
    fun deleteIdea(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.deleteIdea(id)
    }

    @PostMapping("/idea/{id}/like")
    fun likeIdea(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.likeIdea(id)
    }

    @PostMapping("/idea/{id}/dislike")
    fun dislikeIdea(@PathVariable id: Long): ResponseEntity<*> {
        return ideaService.dislikeIdea(id)
    }

    @PostMapping("/idea/{id}/score")
    fun addScore(@PathVariable id: Long, @RequestBody score: ScoreDto): ResponseEntity<*> {
        return ideaService.addScore(id, score)
    }

    @DeleteMapping("/idea/{id}/score")
    fun deleteScore(@PathVariable id: Long, @RequestBody score: ScoreDto): ResponseEntity<*> {
        return ideaService.deleteScore(id, score)
    }
}