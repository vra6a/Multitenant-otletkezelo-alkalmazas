package com.moa.backend.service

import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.ScoreItemRepository
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

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
        logger.info { "MOA-INFO: ${scoreSheet}." }
        scoreSheet.scores?.forEach { score ->
            scoreItemRepository.save(scoreItemMapper.slimDtoToModel(score))
        }

        val ss = scoreSheetRepository.findById(id).orElse(null)

        return ResponseEntity.ok(
            WebResponse<ScoreSheetDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = scoreSheetMapper.modelToDto(ss)
            )
        )
    }
}