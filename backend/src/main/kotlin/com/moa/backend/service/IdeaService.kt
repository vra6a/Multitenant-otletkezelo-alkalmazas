package com.moa.backend.service

import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.TagMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.Tag
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.dto.TagDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class IdeaService {

    @Autowired
    lateinit var ideaRepository: IdeaRepository

    @Autowired
    lateinit var ideaMapper: IdeaMapper

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var tagMapper: TagMapper

    fun getIdea(id: Long): ResponseEntity<*> {
        val idea = ideaRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaMapper.modelToDto(idea)
            )
        )
    }

    fun getIdeaSlim(id: Long): ResponseEntity<*> {
        val idea = ideaRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        return ResponseEntity.ok(
            WebResponse<IdeaSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaMapper.modelToSlimDto(idea)
            )
        )
    }

    fun getIdeas(): ResponseEntity<*> {
        val ideas = ideaRepository.findAll()
        val response: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()

        for( idea in ideas ) {
            idea.let {
                response.add(ideaMapper.modelToSlimDto(idea))
            }
        }
        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createIdea(idea: IdeaDto): ResponseEntity<*> {
        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully created!",
                data = ideaMapper.modelToDto(
                            ideaRepository.saveAndFlush(
                                ideaMapper.dtoToModel(idea)
                            )
                        )
            )
        )
    }

    fun updateIdea(id: Long, idea: IdeaDto): ResponseEntity<*> {
        val originalIdea = ideaRepository.findById(id).orElse(null)
            ?: return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        if(!originalIdea.title.isNullOrEmpty() && originalIdea.title != idea.title) {
            originalIdea.title = idea.title
        }

        if(!originalIdea.description.isNullOrEmpty() && originalIdea.description != idea.description) {
            originalIdea.description = idea.description
        }

        if(originalIdea.status != idea.status) {
            originalIdea.status = idea.status
        }

        val tags: MutableList<Tag> = emptyList<Tag>().toMutableList()
        idea.tags?.forEach{ tag: TagSlimDto ->
                tags.add(tagMapper.slimDtoToModel(tag))
        }
        originalIdea.tags = tags

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully updated!",
                data = ideaMapper.modelToDto(ideaRepository.saveAndFlush(originalIdea))
            )
        )
    }

    fun deleteIdea(id: Long): ResponseEntity<*> {
        kotlin.runCatching {
            ideaRepository.deleteById(id)
        }.onFailure {
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND)
        }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully deleted!",
                data = "Idea successfully deleted!"
            )
        )
    }

    fun likeIdea(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null) ?:
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        val idea = ideaRepository.findById(id).orElse(null) ?:
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        idea.likes?.add(user)
        ideaRepository.saveAndFlush(idea)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea Liked!",
                data = "Idea Liked!"
            )
        )
    }

    fun dislikeIdea(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null) ?:
        return ResponseEntity(
            WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                message = "Authentication error!",
                data = null
            ),
            HttpStatus.NOT_FOUND
        )
        val idea = ideaRepository.findById(id).orElse(null) ?:
        return ResponseEntity(
            WebResponse(
                code = HttpStatus.NOT_FOUND.value(),
                message = "Cannot find Idea with this id $id!",
                data = null
            ),
            HttpStatus.NOT_FOUND
        )

        idea.likes?.remove(user)
        ideaRepository.saveAndFlush(idea)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea disliked!",
                data = "Idea disliked!"
            )
        )
    }
}