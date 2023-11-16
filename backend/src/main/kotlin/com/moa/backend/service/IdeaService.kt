package com.moa.backend.service

import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.TagMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.Status
import com.moa.backend.model.Tag
import com.moa.backend.model.User
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
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

    private val logger = KotlinLogging.logger {}

    fun getIdea(id: Long): ResponseEntity<*> {
        val idea = ideaRepository.findById(id).orElse(null)
        if(idea == null) {
            logger.info { "MOA-INFO: Idea with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: Idea with id: ${id} found." }

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
        if(idea == null) {
            logger.info { "MOA-INFO: Idea with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: ${id} found." }

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

        logger.info { "MOA-INFO: IdeaBoxes found." }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getDefaultJuries(id: Long): ResponseEntity<*> {
        val juries = userRepository.getJuriesByIdeaBoxId(id)

        val response: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        for( jury in juries ) {
            jury.let {
                response.add(userMapper.modelToSlimDto(jury))
            }
        }
        return ResponseEntity.ok(
            WebResponse<MutableList<UserSlimDto>> (
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getReviewedIdeas(): ResponseEntity<*> {
        val ideas = ideaRepository.getReviewedIdeas()
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
        val data = ideaMapper.modelToDto(ideaRepository.saveAndFlush(ideaMapper.dtoToModel(idea)))

        logger.info { "MOA-INFO: Idea created with id: ${data.id}. Idea: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully created!",
                data = data
            )
        )
    }

    fun updateIdea(id: Long, idea: IdeaDto): ResponseEntity<*> {
        val originalIdea = ideaRepository.findById(id).orElse(null)
        if(originalIdea == null) {
            logger.info { "MOA-INFO: Idea with id: ${id} not found" }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

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

        val data = ideaMapper.modelToDto(ideaRepository.saveAndFlush(originalIdea))
        logger.info { "MOA-INFO: Idea edited with id: ${data.id}. Idea: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully updated!",
                data = data
            )
        )
    }

    fun deleteIdea(id: Long): ResponseEntity<*> {
        kotlin.runCatching {
            ideaRepository.deleteById(id)
        }.onFailure {
            logger.info { "MOA-INFO: Idea with id: ${id} not found." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND)
        }

        logger.info { "MOA-INFO: Idea with id: ${id} deleted." }

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
        val user = userRepository.findByEmail(authentication.name).orElse(null)
        if(user == null) {
            logger.info { "MOA-INFO: Authentication error during comment editing. Comment id: ${id}." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        val idea = ideaRepository.findById(id).orElse(null)
        if(idea == null) {
            logger.info { "MOA-INFO: Idea with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        idea.likes?.add(user)
        ideaRepository.saveAndFlush(idea)
        logger.info { "MOA-INFO: Idea with id: ${idea.id} liked by user ${user.email}." }

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
        val user = userRepository.findByEmail(authentication.name).orElse(null)
        if(user == null) {
            logger.info { "MOA-INFO: Authentication error during comment editing. Comment id: ${id}." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        val idea = ideaRepository.findById(id).orElse(null)
        if(idea == null) {
            logger.info { "MOA-INFO: Idea with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        idea.likes?.remove(user)
        ideaRepository.saveAndFlush(idea)
        logger.info { "MOA-INFO: Idea with id: ${idea.id} disliked by user ${user.email}." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea disliked!",
                data = "Idea disliked!"
            )
        )
    }

    fun getIdeasToScore(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)


        val response: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        val ideas = ideaRepository.findAll()

        ideas.forEach{ idea ->
            if(idea.requiredJuries?.contains(user) == true) {
                val ownScoreSheets = idea.scoreSheets.filter { it.owner.email == user.email }

                if(ownScoreSheets.isEmpty()) {
                    response.add(ideaMapper.modelToSlimDto(idea))
                }

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

    fun getScoredIdeas(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)

        val response: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        val ideas = ideaRepository.findAll()

        ideas.forEach{ idea ->
            val usersWhoScored: MutableList<String> = emptyList<String>().toMutableList()
            val requiredJuries: MutableList<String> = emptyList<String>().toMutableList()

            idea.requiredJuries?.forEach { user -> requiredJuries.add(user.email) }
            idea.scoreSheets.forEach { sh ->
                if(sh.templateFor == null) {
                    usersWhoScored.add(sh.owner.email)
                }
            }
            if(usersWhoScored.containsAll(requiredJuries)) {
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
}