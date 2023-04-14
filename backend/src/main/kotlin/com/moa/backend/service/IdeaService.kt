package com.moa.backend.service

import com.moa.backend.converter.IdeaMapper
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.repository.IdeaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class IdeaService {

    @Autowired
    lateinit var ideaRepository: IdeaRepository

    @Autowired
    lateinit var ideaMapper: IdeaMapper

    fun getIdea(id: Long): ResponseEntity<*> {
        val idea = ideaRepository.findById(id).orElse(null)
            ?: return ResponseEntity("Cannot find Idea with id $id!", HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(ideaMapper.modelToDto(idea))
    }

    fun getIdeas(): ResponseEntity<MutableList<IdeaSlimDto>> {
        val ideas = ideaRepository.findAll()
        val response: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()

        for( idea in ideas ) {
            idea.let {
                response.add(ideaMapper.modelToSlimDto(idea))
            }
        }
        return ResponseEntity.ok(response)
    }

    fun createIdea(idea: IdeaDto): ResponseEntity<*> {
        if(idea.id != 0L) {
            return ResponseEntity("Idea with this id ${idea.id} already exists!", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(
            ideaMapper.modelToDto(
                ideaRepository.saveAndFlush(
                    ideaMapper.dtoToModel(idea)
                )
            )
        )
    }

    fun updateIdea(id: Long, idea: IdeaDto): ResponseEntity<*> {
        val originalIdea = ideaRepository.findById(id).orElse(null)
            ?: return ResponseEntity("Cannot find Idea with id $id!", HttpStatus.NOT_FOUND)

        if(!originalIdea.title.isNullOrEmpty() && originalIdea.title != idea.title) {
            originalIdea.title = idea.title
        }

        if(!originalIdea.description.isNullOrEmpty() && originalIdea.description != idea.description) {
            originalIdea.description = idea.description
        }

        if(originalIdea.status != idea.status) {
            originalIdea.status = idea.status
        }
        return ResponseEntity.ok(
            ideaMapper.modelToDto(
                ideaRepository.saveAndFlush(originalIdea)
            )
        )
    }

    fun deleteIdea(id: Long): Any {
        kotlin.runCatching {
            ideaRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity("Nothing to delete! No Idea exists with the id $id!", HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok()
    }
}