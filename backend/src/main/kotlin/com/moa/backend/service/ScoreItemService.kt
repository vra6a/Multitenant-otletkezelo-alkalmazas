package com.moa.backend.service

import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.model.Idea
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.Status
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.ScoreItemRepository
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*

@Service
class ScoreItemService {

    @Autowired
    lateinit var scoreItemRepository: ScoreItemRepository

    @Autowired
    lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @Autowired
    lateinit var scoreItemMapper: ScoreItemMapper

    @Autowired
    lateinit var ideaRepository: IdeaRepository

    @Autowired
    lateinit var ideaMapper: IdeaMapper

    @Autowired
    lateinit var userRepository: UserRepository

    private val logger = KotlinLogging.logger {}

    fun AddScoreItemToScoreSheetTemplate(id: Long, scoreItem: ScoreItemSlimDto): ResponseEntity<*> {

        val scoreSheetTemplate: ScoreSheet = scoreSheetRepository.findById(id).orElse(null)

        scoreSheetTemplate.scores?.add(scoreItemMapper.slimDtoToModel(scoreItem))

        scoreSheetRepository.saveAndFlush(scoreSheetTemplate)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Item Added Successfully",
                data = "Success"
            )
        )
    }

    fun CreateScoreItem(scoreItems: MutableList<ScoreItemDto>, id: Long): ResponseEntity<*> {

        scoreItems.forEach{ item: ScoreItemDto ->
            this.scoreItemRepository.save(scoreItemMapper.dtoToModel(item))
        }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Item Added Successfully",
                data = "ok"
            )
        )
    }

    fun GetScoreSheetById(id: Long): ResponseEntity<*> {

        val sheet = scoreSheetRepository.findById(id).orElse(null)

        return ResponseEntity.ok(
            WebResponse<ScoreSheetDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = scoreSheetMapper.modelToDto(sheet)
            )
        )
    }

    fun saveScoreSheet(scoreSheet: ScoreSheetDto, id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)

        val idea = ideaRepository.findById(scoreSheet.idea!!.id).orElse(null)
        if(idea.scoreSheets.find { sh -> sh.owner.id == user.id } != null) {
            return ResponseEntity.ok(
                WebResponse<ScoreSheetDto>(
                    code = HttpStatus.METHOD_NOT_ALLOWED.value(),
                    message = "Jury already scored this idea!",
                    data = null
                )
            )
        }

        val ss = scoreSheet
        val scores = scoreSheet.scores

        ss.id = 0
        ss.scores= emptyList<ScoreItemDto>().toMutableList()
        ss.idea = ideaMapper.modelToSlimDto(idea)
        ss.templateFor = null
        val tmp = scoreSheetRepository.save(scoreSheetMapper.dtoToModel(ss))
        scores?.forEach{ score ->
            score.id = 0L
            score.scoreSheet = scoreSheetMapper.modelToSlimDto(tmp)
            logger.info { "score: $score" }
            val s = scoreItemRepository.save(scoreItemMapper.dtoToModel(score))
            ss.scores!!.add(scoreItemMapper.modelToDto(s))
        }


        idea.status = Status.REVIEWED
        ideaRepository.saveAndFlush(idea)

        return ResponseEntity.ok(
            WebResponse<ScoreSheetDto>(
                code = HttpStatus.OK.value(),
                message = "Scoring is saved successfully!",
                data = scoreSheet
            )
        )
    }

    fun getScoreSheetsByIdea(id: Long): ResponseEntity<*> {

        val idea = ideaRepository.findById(id).orElse(null)
        val scoreSheets = emptyList<ScoreSheetDto>().toMutableList()

        val template = idea.ideaBox.scoreSheetTemplates[0]
        template.scores?.forEach { score ->
            score.score = 0
        }

        scoreSheets.add(scoreSheetMapper.modelToDto(template))

        idea?.scoreSheets?.forEach { ss ->
            scoreSheets.add(scoreSheetMapper.modelToDto(ss))
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<ScoreSheetDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = scoreSheets
            )
        )
    }
}