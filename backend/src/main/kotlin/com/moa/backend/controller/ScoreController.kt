package com.moa.backend.controller

import com.moa.backend.service.ScoreService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class ScoreController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var scoreService: ScoreService

    @GetMapping("score/getIdeas")
    fun getIdeas(): ResponseEntity<*> {
        return scoreService.getIdeas()
    }

    @GetMapping("score/getScoredIdeaBoxes")
    fun getScoredIdeaBoxes(): ResponseEntity<*> {
        return scoreService.getScoredIdeaBoxes()
    }

}