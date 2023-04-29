package com.moa.backend.service

import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.utility.WebResponse
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
    lateinit var ideaBoxMapper: IdeaBoxMapper


    fun getIdeaBox(id: Long): ResponseEntity<*> {
        val ideaBox =  ideaBoxRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
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
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
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
                response.add(ideaBoxMapper.modelToSlimDto(ideaBox))
            }
        }
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
        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully created!",
                data = ideaBoxMapper.modelToDto(
                            ideaBoxRepository.saveAndFlush(
                                ideaBoxMapper.dtoToModel(box)
                            )
                        )
            )
        )
    }

    fun updateIdeaBox(id: Long, box: IdeaBoxDto): ResponseEntity<*> {
        val originalBox = ideaBoxRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

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

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully updated!",
                data = ideaBoxMapper.modelToDto(ideaBoxRepository.saveAndFlush(originalBox))
            )
        )
    }

    fun deleteIdeaBox(id: Long): ResponseEntity<*> {
        kotlin.runCatching {
            ideaBoxRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea Box exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully deleted!",
                data = "Idea Box successfully deleted!"
            )
        )
    }
}