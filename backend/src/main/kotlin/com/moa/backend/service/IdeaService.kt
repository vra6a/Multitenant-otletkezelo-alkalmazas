package com.moa.backend.service

import com.moa.backend.model.Idea
import com.moa.backend.repository.IdeaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IdeaService {

    @Autowired
    lateinit var ideaRepository: IdeaRepository

    fun getIdea(id: Long): Idea {
        return ideaRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find Idea with this id!")
    }

    fun getIdeas(): List<Idea> {
        return ideaRepository.findAll()
    }

    fun createIdea(idea: Idea): Idea {
        if(idea.id != 0L) {
            throw Exception("Idea with this id ${idea.id} already exists!")
        }
        return ideaRepository.saveAndFlush(idea)
    }

    fun updateIdea(id: Long, idea: Idea): Idea {
        val originalIdea = ideaRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find Idea with this id!")

        if(!originalIdea.title.isNullOrEmpty() && originalIdea.title != idea.title) {
            originalIdea.title = idea.title
        }

        if(!originalIdea.description.isNullOrEmpty() && originalIdea.description != idea.description) {
            originalIdea.description = idea.description
        }

        if(originalIdea.status != idea.status) {
            originalIdea.status = idea.status
        }
        return ideaRepository.saveAndFlush(originalIdea)
    }

    fun deleteIdea(id: Long): Any {
        kotlin.runCatching {
            ideaRepository.deleteById(id)
        }.onFailure {
            throw Exception("Nothing to delete! No Idea exists with the id ${id}.")
        }

        return "OK"
    }
}