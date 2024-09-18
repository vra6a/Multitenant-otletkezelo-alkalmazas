package com.moa.backend.controller

import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.service.IdeaBoxService
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
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
    fun getIdeaBoxes(
        @RequestParam("search", defaultValue = "") s: String,
        @RequestParam("sort", defaultValue = "") sort: String,
        @RequestParam("page", defaultValue = "1") page: Int,
        @RequestParam("items", defaultValue = "12") items: Int
    ): ResponseEntity<*> {
        var direction = Sort.unsorted()
        when(sort) {
            "newest" -> direction = Sort.by(Sort.Direction.DESC, "startDate")
            "oldest" -> direction = Sort.by(Sort.Direction.ASC, "startDate")
            "closing" -> direction = Sort.by(Sort.Direction.ASC, "endDate")
        }

        return ideaBoxService.getIdeaBoxes(s, PageRequest.of(page-1, items, direction))
    }

    @GetMapping("/idea-box-count")
    fun getIdeaBoxCount(): ResponseEntity<*> {
        return ideaBoxService.getIdeaBoxCount()
    }

    @GetMapping("/idea-box/{id}")
    fun getIdeaBox(@PathVariable id: Long): ResponseEntity<*> {
        return  ideaBoxService.getIdeaBox(id)
    }

    @GetMapping("/idea-box/{id}/scored")
    fun getScoredIdeaCountByIdeaBox(@PathVariable id: Long): ResponseEntity<*> {
        return  ideaBoxService.getScoredIdeaCountByIdeaBox(id)
    }

    @GetMapping("/idea-box/slim/{id}")
    fun getIdeaBoxSlim(@PathVariable id: Long): ResponseEntity<*> {
        return  ideaBoxService.getIdeaBoxSlim(id)
    }

    @PostMapping("/idea-box")
    fun createIdeaBox(@RequestBody box: IdeaBoxDto): ResponseEntity<*> {
        return ideaBoxService.createIdeaBox(box)
    }

    @PostMapping("/idea-box/{id}/createScoreSheetTemplate")
    fun createScoreSheetTemplate(@RequestBody scoreSheet: ScoreSheetDto): ResponseEntity<*> {
        return ideaBoxService.createScoreSheetTemplate(scoreSheet)
    }

    @PutMapping("/idea-box/{id}")
    fun updateIdeaBox(@PathVariable id: Long, @RequestBody box: IdeaBoxDto): ResponseEntity<*> {
        return ideaBoxService.updateIdeaBox(id, box)
    }

    @DeleteMapping("/idea-box/{id}")
    fun deleteIdeaBox(@PathVariable id: Long): ResponseEntity<*> {
        return ideaBoxService.deleteIdeaBox(id)
    }

    @GetMapping("/idea-box/{id}/checkScoreSheets")
    fun checkIfIdeaBoxHasAllRequiredScoreSheets(@PathVariable id: Long): ResponseEntity<*> {
        return ideaBoxService.checkIfIdeaBoxHasAllRequiredScoreSheets(id)
    }

    @GetMapping("/idea-box/scoreSheets/{ideaBoxId}")
    fun getAverageScoresForIdeaBoxByScore(@PathVariable ideaBoxId: Long): ResponseEntity<*> {
        return ideaBoxService.getAverageScoresForIdeaBoxByScore(ideaBoxId)
    }

    @GetMapping("/idea-box/{id}/isReadyToClose")
    fun ideaBoxReadyToClose(@PathVariable id: Long): ResponseEntity<*> {
        return ideaBoxService.ideaBoxReadyToClose(id)
    }

    @PostMapping("/idea-box/{id}/close")
    fun closeIdeaBox(@PathVariable id: Long): ResponseEntity<*> {
        return ideaBoxService.closeIdeaBox(id)
    }

    @GetMapping("/idea-box/closedIdeaBoxes")
    fun getClosedIdeaBoxes(): ResponseEntity<*> {
        return ideaBoxService.getClosedIdeaBoxes()
    }
}