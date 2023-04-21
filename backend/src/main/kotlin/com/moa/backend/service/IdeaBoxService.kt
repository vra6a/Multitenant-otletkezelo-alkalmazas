package com.moa.backend.service

import com.moa.backend.converter.IdeaBoxMapper
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.repository.IdeaBoxRepository
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
            ?: return ResponseEntity("Cannot find IdeaBox with id $id!", HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(ideaBoxMapper.modelToDto(ideaBox))
    }

    fun getIdeaBoxes(s: String, pageable: Pageable): ResponseEntity<MutableList<IdeaBoxSlimDto>> {
        val ideaBoxes = ideaBoxRepository.search(s, pageable)
        val response: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()

        for (ideaBox in ideaBoxes) {
            ideaBox.let {
                response.add(ideaBoxMapper.modelToSlimDto(ideaBox))
            }
        }
        return ResponseEntity.ok(response)
    }

    fun getIdeaBoxCount(): Int {
        return ideaBoxRepository.findAll().size
    }

    fun createIdeaBox(box: IdeaBoxDto): ResponseEntity<*> {
        return ResponseEntity.ok(
            ideaBoxMapper.modelToDto(
                ideaBoxRepository.saveAndFlush(
                    ideaBoxMapper.dtoToModel(box)
                )
            )
        )
    }

    fun updateIdeaBox(id: Long, box: IdeaBoxDto): ResponseEntity<*> {
        val originalBox = ideaBoxRepository.findById(id).orElse(null)
            ?: return ResponseEntity("Cannot find IdeaBox with id $id!", HttpStatus.NOT_FOUND)

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
            ideaBoxMapper.modelToDto(
                ideaBoxRepository.saveAndFlush(originalBox)
            )
        )
    }

    fun deleteIdeaBox(id: Long): Any {
        kotlin.runCatching {
            ideaBoxRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity("Nothing to delete! No IdeaBox exists with the id $id!", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok()
    }
}