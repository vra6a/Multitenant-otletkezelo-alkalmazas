package com.moa.backend.service

import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.model.Idea
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import com.moa.backend.model.dto.utility.BulkIdeaDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

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

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun getIdeas(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        )
            .setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "User not authenticated!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val ideas = currentEntityManager.createQuery(
            "SELECT i FROM Idea i WHERE :user MEMBER OF i.requiredJuries", Idea::class.java
        )
            .setParameter("user", user)
            .resultList

        val response: MutableList<BulkIdeaDto> = mutableListOf()
        val ideasGroupedByIdeaBoxId = ideas.groupBy { it.ideaBox }

        ideasGroupedByIdeaBoxId.forEach { (ideaBox, ideas) ->
            response.add(BulkIdeaDto(ideaBoxMapper.modelToSlimDto(ideaBox), ideaMapper.ModelListToSlimDto(ideas)))
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBoxes = currentEntityManager.createQuery(
            "SELECT ib FROM IdeaBox ib JOIN ib.ideas i JOIN i.scoreSheets s GROUP BY ib", IdeaBox::class.java
        ).resultList

        val response: MutableList<IdeaBoxSlimDto> = ideaBoxMapper.ModelListToSlimDto(ideaBoxes)

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaBoxSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }
}