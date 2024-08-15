package com.moa.backend.service

import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.model.dto.utility.BulkIdeaDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class ScoreService {

    @Autowired
    private lateinit var ideaBoxRepository: IdeaBoxRepository

    @Autowired
    private lateinit var ideaRepository: IdeaRepository

    @Autowired
    private lateinit var ideaBoxMapper: IdeaBoxMapper

    @Autowired
    private lateinit var ideaMapper: IdeaMapper

    @Autowired
    lateinit var userRepository: UserRepository


    fun getIdeas(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)

        val response: MutableList<BulkIdeaDto> = emptyList<BulkIdeaDto>().toMutableList()
        val ideas = ideaRepository.findByRequiredJuriesContaining(user)

        val ideasGroupedByIdeaBoxId = ideas.groupBy { it.ideaBox }

        ideasGroupedByIdeaBoxId.forEach { ideabox ->
            response.add(BulkIdeaDto(ideaBoxMapper.modelToSlimDto(ideabox.key), ideaMapper.ModelListToSlimDto(ideabox.value)))
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<BulkIdeaDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getScoredIdeaBoxes(): ResponseEntity<*> {
        var response: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()

        response = ideaBoxMapper.ModelListToSlimDto(ideaBoxRepository.findIdeaBoxesWithIdeasHavingScoreSheets())

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaBoxSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }


}