package com.moa.backend.service

import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import com.moa.backend.repository.IdeaBoxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IdeaBoxService {

    @Autowired
    lateinit var ideaBoxRepository: IdeaBoxRepository

    fun getIdeaBox(id: Long): IdeaBox {
        return ideaBoxRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find IdeaBox with this id!")
    }

    fun getIdeaBoxes(): List<IdeaBox> {
        return ideaBoxRepository.findAll()
    }

    fun createIdeaBox(box: IdeaBox): IdeaBox {
        if(box.id != 0L) {
            throw Exception("IdeaBox with this id ${box.id} already exists!")
        }
        return ideaBoxRepository.saveAndFlush(box)
    }

    fun updateIdeaBox(id: Long, box: IdeaBox): IdeaBox {
        val originalBox = ideaBoxRepository.findById(id).orElse(null)
            ?: throw Exception("Cannot find IdeaBox with this id!")

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

        return ideaBoxRepository.saveAndFlush(originalBox)
    }

    fun deleteIdeaBox(id: Long): Any {
        kotlin.runCatching {
            ideaBoxRepository.deleteById(id)
        }.onFailure {
            throw Exception("Nothing to delete! No IdeaBox exists with the id ${id}.")
        }

        return "OK"
    }
}