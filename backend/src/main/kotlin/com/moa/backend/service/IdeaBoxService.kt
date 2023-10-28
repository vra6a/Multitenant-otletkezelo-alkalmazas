package com.moa.backend.service

import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class IdeaBoxService {

    @Autowired
    lateinit var ideaBoxRepository: IdeaBoxRepository

    @Autowired
    lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    lateinit var ideaBoxMapper: IdeaBoxMapper

    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper

    private val logger = KotlinLogging.logger {}


    fun getIdeaBox(id: Long): ResponseEntity<*> {
        val ideaBox =  ideaBoxRepository.findById(id).orElse(null)
        if(ideaBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: ${id} found." }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaBoxMapper.modelToDto(ideaBox)
            )
        )
    }

    fun getIdeaBoxSlim(id: Long): ResponseEntity<*> {
        val ideaBox =  ideaBoxRepository.findById(id).orElse(null)
        if(ideaBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: ${id} found." }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaBoxMapper.modelToSlimDto(ideaBox)
            )
        )
    }

    fun getIdeaBoxes(s: String, pageable: Pageable): ResponseEntity<*> {
        val ideaBoxes = ideaBoxRepository.search(s, pageable)
        val response: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()

        for (ideaBox in ideaBoxes) {
            ideaBox.let {
                val ideaBoxSlimDto = ideaBoxMapper.modelToSlimDto(ideaBox)
                ideaBoxSlimDto.draft = ideaBox.scoreSheetTemplates.isEmpty()
                response.add(ideaBoxSlimDto)
            }
        }

        logger.info { "MOA-INFO: IdeaBoxes found." }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaBoxSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getIdeaBoxCount(): ResponseEntity<*> {
        return ResponseEntity.ok(
            WebResponse<Int>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaBoxRepository.findAll().size
            )
        )
    }

    fun createIdeaBox(box: IdeaBoxDto): ResponseEntity<*> {
        val data = ideaBoxMapper.modelToDto(ideaBoxRepository.save(ideaBoxMapper.dtoToModel(box)))

        logger.info { "MOA-INFO: IdeaBox created with id: ${data.id}. IdeaBox: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully created!",
                data = data
            )
        )
    }

    fun updateIdeaBox(id: Long, box: IdeaBoxDto): ResponseEntity<*> {
        val originalBox = ideaBoxRepository.findById(id).orElse(null)
        if(originalBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: ${id} not found" }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find IdeaBox with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        if(!originalBox.name.isNullOrEmpty() && originalBox.name != box.name) {
            originalBox.name = box.name
        }

        if(!originalBox.description.isNullOrEmpty() && originalBox.description != box.description) {
            originalBox.description = box.description
        }

        if(originalBox.startDate != box.startDate) {
            originalBox.startDate = box.startDate
        }

        if(originalBox.endDate != box.endDate) {
            originalBox.endDate = box.endDate
        }

        val data = ideaBoxMapper.modelToDto(ideaBoxRepository.save(originalBox))
        logger.info { "MOA-INFO: IdeaBox edited with id: ${data.id}. IdeaBox: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully updated!",
                data = data
            )
        )
    }

    fun deleteIdeaBox(id: Long): ResponseEntity<*> {
        kotlin.runCatching {
            ideaBoxRepository.deleteById(id)
        }.onFailure {
            logger.info { "MOA-INFO: IdeaBox with id: ${id} not found." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea Box exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND)
        }

        logger.info { "MOA-INFO: IdeaBox with id: ${id} deleted." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully deleted!",
                data = "Idea Box successfully deleted!"
            )
        )
    }

    fun createScoreSheetTemplate(scoreSheet: ScoreSheetDto): ResponseEntity<*> {

        val ss = this.scoreSheetRepository.saveAndFlush(scoreSheetMapper.initializeScoreSheet(scoreSheet))
        logger.info { "MOA-INFO: Empty ScoreSheet created with id: ${ss.id}" }
        return ResponseEntity.ok(
            WebResponse<ScoreSheetSlimDto>(
                code = HttpStatus.OK.value(),
                message = "ScoreSheet Created, ready to be filled",
                data = scoreSheetMapper.modelToSlimDto(ss)
            )
        )
    }
}