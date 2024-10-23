package com.moa.backend.service

import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.TagMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.*
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.Functions
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class IdeaService {

    @Autowired
    private lateinit var ideaBoxRepository: IdeaBoxRepository

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

    @Autowired
    lateinit var functions: Functions

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun getIdea(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val idea = currentEntityManager.find(Idea::class.java, id)
            ?: run {
                logger.info { "MOA-INFO: Idea with id: $id not found." }
                return ResponseEntity(
                    WebResponse(
                        code = HttpStatus.NOT_FOUND.value(),
                        message = "Cannot find Idea with this id $id!",
                        data = null
                    ),
                    HttpStatus.NOT_FOUND
                )
            }

        logger.info { "MOA-INFO: Idea with id: $id found." }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaMapper.modelToDto(idea)
            )
        )
    }

    fun getIdeaSlim(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val idea = currentEntityManager.find(Idea::class.java, id)
            ?: run {
                logger.info { "MOA-INFO: Idea with id: $id not found." }
                return ResponseEntity(
                    WebResponse(
                        code = HttpStatus.NOT_FOUND.value(),
                        message = "Cannot find Idea with this id $id!",
                        data = null
                    ),
                    HttpStatus.NOT_FOUND
                )
            }

        logger.info { "MOA-INFO: Idea with id: $id found." }

        return ResponseEntity.ok(
            WebResponse<IdeaSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaMapper.modelToSlimDto(idea)
            )
        )
    }

    fun getIdeas(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideas = currentEntityManager.createQuery("SELECT i FROM Idea i", Idea::class.java).resultList
        val response: MutableList<IdeaSlimDto> = ideas.map { ideaMapper.modelToSlimDto(it) }.toMutableList()

        logger.info { "MOA-INFO: Ideas found." }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getDefaultJuries(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val juries = currentEntityManager.createQuery(
            "SELECT u FROM User u JOIN u.ideas i WHERE i.ideaBox.id = :ideaBoxId", User::class.java
        )
            .setParameter("ideaBoxId", id)
            .resultList

        val response: MutableList<UserSlimDto> = juries.map { userMapper.modelToSlimDto(it) }.toMutableList()

        return ResponseEntity.ok(
            WebResponse<MutableList<UserSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun getReviewedIdeas(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideas = currentEntityManager.createQuery(
            "SELECT i FROM Idea i WHERE i.status = :status", Idea::class.java
        )
            .setParameter("status", Status.REVIEWED)
            .resultList

        val response: MutableList<IdeaSlimDto> = ideas.map { ideaMapper.modelToSlimDto(it) }.toMutableList()

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun createIdea(idea: IdeaDto): ResponseEntity<*> {
        val saveIdea = ideaMapper.dtoToModel(idea)

        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = ideaBoxRepository.findById(saveIdea.ideaBox.id).orElse(null)
            ?: return ResponseEntity.ok(
                WebResponse<IdeaDto>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "IdeaBox Not found!",
                    data = null
                )
            )

        val currentLocalDate = LocalDate.now()
        if (ideaBox.endDate.after(functions.localDateToDate(currentLocalDate))) {
            currentEntityManager.persist(saveIdea)
            currentEntityManager.flush()
        } else {
            return ResponseEntity.ok(
                WebResponse<IdeaDto>(
                    code = HttpStatus.METHOD_NOT_ALLOWED.value(),
                    message = "IdeaBox no longer accepts ideas! End Date: ${ideaBox.endDate}, Current Date: $currentLocalDate",
                    data = null
                )
            )
        }

        logger.info { "MOA-INFO: Idea created with id: ${saveIdea.id}." }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea successfully created!",
                data = ideaMapper.modelToDto(saveIdea)
            )
        )
    }

    fun updateIdea(id: Long, idea: IdeaDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val originalIdea = currentEntityManager.find(Idea::class.java, id)
            ?: run {
                logger.info { "MOA-INFO: Idea with id: $id not found." }
                return ResponseEntity(
                    WebResponse(
                        code = HttpStatus.NOT_FOUND.value(),
                        message = "Cannot find Idea with this id $id!",
                        data = null
                    ),
                    HttpStatus.NOT_FOUND
                )
            }

        originalIdea.title = idea.title.takeIf { !it.isNullOrEmpty() && it != originalIdea.title } ?: originalIdea.title
        originalIdea.description = idea.description.takeIf { !it.isNullOrEmpty() && it != originalIdea.description } ?: originalIdea.description
        originalIdea.status = idea.status.takeIf { it != originalIdea.status } ?: originalIdea.status

        originalIdea.tags = idea.tags?.map { tagMapper.slimDtoToModel(it) }?.toMutableList() ?: mutableListOf()

        currentEntityManager.merge(originalIdea)

        val data = ideaMapper.modelToDto(originalIdea)
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
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val idea = currentEntityManager.find(Idea::class.java, id)
        return if (idea != null) {
            currentEntityManager.remove(idea)
            logger.info { "MOA-INFO: Idea with id: $id deleted." }
            ResponseEntity.ok(
                WebResponse<String>(
                    code = HttpStatus.OK.value(),
                    message = "Idea successfully deleted!",
                    data = "Idea successfully deleted!"
                )
            )
        } else {
            logger.info { "MOA-INFO: Idea with id: $id not found." }
            ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }
    }

    fun likeIdea(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        ).setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            logger.info { "MOA-INFO: Authentication error during liking idea. Idea id: $id." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val idea = currentEntityManager.find(Idea::class.java, id)
        if (idea == null) {
            logger.info { "MOA-INFO: Idea with id: $id not found." }
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
        currentEntityManager.merge(idea)
        logger.info { "MOA-INFO: Idea with id: ${idea.id} liked by user $userEmail." }

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
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        ).setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            logger.info { "MOA-INFO: Authentication error during disliking idea. Idea id: $id." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "Authentication error!",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        val idea = currentEntityManager.find(Idea::class.java, id)
        if (idea == null) {
            logger.info { "MOA-INFO: Idea with id: $id not found." }
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
        currentEntityManager.merge(idea)
        logger.info { "MOA-INFO: Idea with id: ${idea.id} disliked by user $userEmail." }

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
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        ).setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            logger.info { "MOA-INFO: Authentication error. User not found." }
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
            "SELECT i FROM Idea i", Idea::class.java
        ).resultList

        val ideasToScore: MutableList<IdeaSlimDto> = ideas.filter { idea ->
            idea.requiredJuries?.contains(user) == true &&
                    idea.scoreSheets.none { it.owner.email == userEmail }
        }.map { ideaMapper.modelToSlimDto(it) }.toMutableList()

        logger.info { "MOA-INFO: Found ${ideasToScore.size} ideas to score for user $userEmail." }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "Ideas ready for scoring retrieved successfully.",
                data = ideasToScore
            )
        )
    }

    fun getScoredIdeas(): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        ).setParameter("email", userEmail)
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

        val scoredIdeas: MutableList<IdeaSlimDto> = mutableListOf()

        val ideas = currentEntityManager.createQuery(
            "SELECT i FROM Idea i", Idea::class.java
        ).resultList

        ideas.forEach { idea ->
            val requiredJuryEmails = idea.requiredJuries?.map { it.email } ?: emptyList()
            val usersWhoScored = idea.scoreSheets
                .filter { it.templateFor == null }
                .map { it.owner.email }

            if (usersWhoScored.containsAll(requiredJuryEmails)) {
                scoredIdeas.add(ideaMapper.modelToSlimDto(idea))
            }
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "Scored ideas retrieved successfully.",
                data = scoredIdeas
            )
        )
    }

    fun approveIdea(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        )
            .setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            logger.info { "MOA-INFO: Authentication error during Approving." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "User not authenticated.",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        if (user.role != Role.ADMIN) {
            logger.info { "MOA-INFO: Authentication error during Approving. User has no Admin role." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.FORBIDDEN.value(),
                    message = "You do not have permission to approve ideas.",
                    data = null
                ),
                HttpStatus.FORBIDDEN
            )
        }

        val idea = currentEntityManager.createQuery(
            "SELECT i FROM Idea i WHERE i.id = :id", Idea::class.java
        )
            .setParameter("id", id)
            .resultList
            .firstOrNull()

        if (idea == null) {
            logger.info { "MOA-INFO: No idea found with id: $id." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        idea.status = Status.APPROVED
        currentEntityManager.merge(idea)

        logger.info { "MOA-INFO: Idea with id: ${idea.id} approved by user ${user.email}." }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea was approved successfully.",
                data = ideaMapper.modelToDto(idea)
            )
        )
    }

    fun denyIdea(id: Long): ResponseEntity<*> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val user = currentEntityManager.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User::class.java
        )
            .setParameter("email", userEmail)
            .resultList
            .firstOrNull()

        if (user == null) {
            logger.info { "MOA-INFO: Authentication error during Denying." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.UNAUTHORIZED.value(),
                    message = "User not authenticated.",
                    data = null
                ),
                HttpStatus.UNAUTHORIZED
            )
        }

        if (user.role != Role.ADMIN) {
            logger.info { "MOA-INFO: Authentication error during Denying. User has no Admin role." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.FORBIDDEN.value(),
                    message = "You do not have permission to deny ideas.",
                    data = null
                ),
                HttpStatus.FORBIDDEN
            )
        }

        val idea = currentEntityManager.createQuery(
            "SELECT i FROM Idea i WHERE i.id = :id", Idea::class.java
        )
            .setParameter("id", id)
            .resultList
            .firstOrNull()

        if (idea == null) {
            logger.info { "MOA-INFO: No idea found with id: $id." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        idea.status = Status.DENIED
        currentEntityManager.merge(idea)

        logger.info { "MOA-INFO: Idea with id: ${idea.id} denied by user ${user.email}." }

        return ResponseEntity.ok(
            WebResponse<IdeaDto>(
                code = HttpStatus.OK.value(),
                message = "Idea was denied successfully.",
                data = ideaMapper.modelToDto(idea)
            )
        )
    }
}