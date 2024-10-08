package com.moa.backend.controller

import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.service.ScoreItemService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@RequestMapping("/api")
class ScoreSheetController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var scoreItemService: ScoreItemService

    @PostMapping("/scoreSheet/{id}")
    fun AddScoreItemToScoreSheetTemplate(@PathVariable id: Long, @RequestBody item: ScoreItemSlimDto): ResponseEntity<*> {
        return scoreItemService.AddScoreItemToScoreSheetTemplate(id, item)
    }

    @PostMapping("/scoreSheet/create/{id}")
    fun CreateScoreItem(@RequestBody items: MutableList<ScoreItemDto>, @PathVariable id: Long): ResponseEntity<*> {
        return scoreItemService.CreateScoreItem(items, id)
    }

    @PostMapping("/scoreSheet/{id}/save")
    fun SaveScoreSheet(@RequestBody scoreSheet: ScoreSheetDto, @PathVariable id: Long): ResponseEntity<*> {
        logger.info { "MOA-INFO: ${scoreSheet}." }
        return scoreItemService.saveScoreSheet(scoreSheet, id)
    }

    @GetMapping("/scoreSheet/{id}")
    fun getScoreSheetById(@PathVariable id: Long): ResponseEntity<*> {
        return scoreItemService.GetScoreSheetById(id)
    }
}