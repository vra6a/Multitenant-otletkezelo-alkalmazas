package com.moa.backend.service

import com.moa.backend.mapper.IdeaBoxMapper
import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.mapper.UserMapper
import com.moa.backend.model.*
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.IdeaBoxRepository
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.utility.Functions
import com.moa.backend.utility.IdeaScoreSheets
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class IdeaBoxService {

    @Autowired
    private lateinit var ideaMapper: IdeaMapper

    @Autowired
    lateinit var ideaBoxRepository: IdeaBoxRepository

    @Autowired
    lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    lateinit var ideaBoxMapper: IdeaBoxMapper

    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var functions: Functions

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager


    fun getIdeaBox(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)

        if (ideaBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: ${id} not found." }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: ${id} found." }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaBoxMapper.modelToDto(ideaBox)
            )
        )
    }

    fun getIdeaBoxSlim(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)

        if (ideaBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: $id not found." }
            return ResponseEntity(
                WebResponse<IdeaBoxSlimDto>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find Idea Box with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: $id found." }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxSlimDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = ideaBoxMapper.modelToSlimDto(ideaBox)
            )
        )
    }

    fun getIdeaBoxes(searchTerm: String, pageable: Pageable): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBoxes = currentEntityManager.createQuery(
            "SELECT ib FROM IdeaBox ib WHERE ib.name LIKE :searchTerm",
            IdeaBox::class.java
        )
            .setParameter("searchTerm", "%$searchTerm%")
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList

        val response = ideaBoxes.map { ideaBox ->
            ideaBoxMapper.modelToSlimDto(ideaBox).apply {
                draft = ideaBox.scoreSheetTemplates.isEmpty()
            }
        }

        logger.info { "MOA-INFO: IdeaBoxes found." }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaBoxSlimDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response.toMutableList()
            )
        )
    }

    fun getIdeaBoxCount(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val query = currentEntityManager.createQuery("SELECT COUNT(ib) FROM IdeaBox ib")
        val count = query.singleResult as Long

        return ResponseEntity.ok(
            WebResponse<Int>(
                code = HttpStatus.OK.value(),
                message = "",
                data = count.toInt()
            )
        )
    }

    fun createIdeaBox(box: IdeaBoxDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBoxEntity = ideaBoxMapper.dtoToModel(box)

        currentEntityManager.transaction.begin()
        currentEntityManager.persist(ideaBoxEntity)
        currentEntityManager.transaction.commit()

        val data = ideaBoxMapper.modelToDto(ideaBoxEntity)

        logger.info { "MOA-INFO: IdeaBox created with id: ${data.id}. IdeaBox: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully created!",
                data = data
            )
        )
    }

    fun updateIdeaBox(id: Long, box: IdeaBoxDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val originalBox = currentEntityManager.find(IdeaBox::class.java, id)

        if (originalBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: $id not found" }
            return ResponseEntity(
                WebResponse(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find IdeaBox with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        originalBox.name = box.name.takeIf { !it.isNullOrEmpty() } ?: originalBox.name
        originalBox.description = box.description.takeIf { !it.isNullOrEmpty() } ?: originalBox.description
        originalBox.startDate = box.startDate ?: originalBox.startDate
        originalBox.endDate = box.endDate ?: originalBox.endDate

        currentEntityManager.transaction.begin()
        currentEntityManager.merge(originalBox)
        currentEntityManager.transaction.commit()

        val data = ideaBoxMapper.modelToDto(originalBox)

        logger.info { "MOA-INFO: IdeaBox edited with id: ${data.id}. IdeaBox: $data" }

        return ResponseEntity.ok(
            WebResponse<IdeaBoxDto>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully updated!",
                data = data
            )
        )
    }

    fun deleteIdeaBox(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)

        if (ideaBox == null) {
            logger.info { "MOA-INFO: IdeaBox with id: $id not found." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Nothing to delete! No Idea Box exists with the id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        kotlin.runCatching {
            currentEntityManager.transaction.begin()
            currentEntityManager.remove(ideaBox)
            currentEntityManager.transaction.commit()
        }.onFailure {
            logger.error { "MOA-ERROR: Failed to delete IdeaBox with id: $id." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = "Failed to delete Idea Box with id $id!",
                    data = null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        logger.info { "MOA-INFO: IdeaBox with id: $id deleted." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Idea Box successfully deleted!",
                data = "Idea Box successfully deleted!"
            )
        )
    }

    fun createScoreSheetTemplate(scoreSheet: ScoreSheetDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val scoreSheetEntity = scoreSheetMapper.initializeScoreSheet(scoreSheet)

        kotlin.runCatching {
            currentEntityManager.transaction.begin()
            currentEntityManager.persist(scoreSheetEntity)
            currentEntityManager.transaction.commit()
        }.onFailure {
            logger.error { "MOA-ERROR: Failed to create ScoreSheet." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = "Failed to create ScoreSheet!",
                    data = null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        logger.info { "MOA-INFO: Empty ScoreSheet created with id: ${scoreSheetEntity.id}" }

        return ResponseEntity.ok(
            WebResponse<ScoreSheetSlimDto>(
                code = HttpStatus.OK.value(),
                message = "ScoreSheet Created, ready to be filled",
                data = scoreSheetMapper.modelToSlimDto(scoreSheetEntity)
            )
        )
    }

    fun getScoredIdeaCountByIdeaBox(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val count = kotlin.runCatching {
            currentEntityManager.createQuery(
                "SELECT COUNT(i) FROM Idea i WHERE i.ideaBox.id = :ideaBoxId AND i.scoreSheets IS NOT EMPTY",
                Long::class.java
            )
                .setParameter("ideaBoxId", id)
                .singleResult
        }.getOrElse {
            logger.error { "MOA-ERROR: Error retrieving scored idea count for IdeaBox with id: $id." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = "Error retrieving scored idea count.",
                    data = null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        logger.info { "MOA-INFO: Scored idea count for IdeaBox with id: $id is $count." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "",
                data = count.toString()
            )
        )
    }

    fun findRequiredJuriesByIdeaBoxId(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val requiredJuries = kotlin.runCatching {
            currentEntityManager.createQuery(
                "SELECT u FROM User u JOIN u.ideaBoxes ib WHERE ib.id = :ideaBoxId AND u.role = 'JURY'",
                User::class.java
            )
                .setParameter("ideaBoxId", id)
                .resultList
        }.getOrElse {
            logger.error { "MOA-ERROR: Error retrieving required juries for IdeaBox with id: $id." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    message = "Error retrieving required juries.",
                    data = null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }

        logger.info { "MOA-INFO: Found ${requiredJuries.size} required juries for IdeaBox with id: $id." }

        return ResponseEntity.ok(
            WebResponse(
                code = HttpStatus.OK.value(),
                message = "",
                data = userMapper.ModelListToSlimDto(requiredJuries)
            )
        )
    }

    fun checkIfIdeaBoxHasAllRequiredScoreSheets(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)
            ?: return ResponseEntity(
                WebResponse<Boolean>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "IdeaBox with id $id not found.",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        val data = areAllIdeasScoredByRequiredJuries(ideaBox)

        return ResponseEntity.ok(
            WebResponse<Boolean>(
                code = HttpStatus.OK.value(),
                message = "",
                data = data
            )
        )
    }

    fun areAllIdeasScoredByRequiredJuries(ideaBox: IdeaBox): Boolean {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideas = currentEntityManager.createQuery(
            "SELECT i FROM Idea i WHERE i.ideaBox.id = :ideaBoxId",
            Idea::class.java
        ).setParameter("ideaBoxId", ideaBox.id).resultList

        for (idea in ideas) {
            val requiredJuries = idea.requiredJuries
            val scoresheets = idea.scoreSheets

            requiredJuries?.forEach { jury ->
                val juryHasScoreSheet = scoresheets.any { it.owner.id == jury.id }
                if (!juryHasScoreSheet) {
                    return false
                }
            }
        }
        return true
    }

    fun getAverageScoresForIdeaBoxByScore(ideaBoxId: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val response: MutableList<IdeaScoreSheets> = mutableListOf()

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, ideaBoxId)
            ?: return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Idea Box with id $ideaBoxId not found.",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        ideaBox.ideas.forEach { idea ->
            response.add(IdeaScoreSheets(ideaMapper.modelToSlimDto(idea), scoreSheetMapper.modelListToDto(idea.scoreSheets)))
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaScoreSheets>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = response
            )
        )
    }

    fun ideaBoxReadyToClose(id: Long): ResponseEntity<*> {
        val currentLocalDate = LocalDate.now()
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)
            ?: return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "IdeaBox with id $id not found.",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        if (ideaBox.endDate.after(functions.localDateToDate(currentLocalDate))) {
            return ResponseEntity.ok(
                WebResponse<Boolean>(
                    code = HttpStatus.OK.value(),
                    message = "IdeaBox is not ready to close because the end date has not been reached.",
                    data = false
                )
            )
        }

        val errorIdeas = ideaBox.ideas.filter { idea ->
            idea.status == Status.REVIEWED || idea.status == Status.SUBMITTED
        }

        if (errorIdeas.isNotEmpty()) {
            return ResponseEntity.ok(
                WebResponse<Boolean>(
                    code = HttpStatus.OK.value(),
                    message = "IdeaBox is not ready to close because there are ideas with status REVIEWED or SUBMITTED.",
                    data = false
                )
            )
        }

        return ResponseEntity.ok(
            WebResponse<Boolean>(
                code = HttpStatus.OK.value(),
                message = "IdeaBox is ready to close.",
                data = true
            )
        )
    }

    fun closeIdeaBox(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val ideaBox = currentEntityManager.find(IdeaBox::class.java, id)
            ?: return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "IdeaBox with id $id not found.",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        ideaBox.isSclosed = true
        currentEntityManager.persist(ideaBox)

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "IdeaBox was closed successfully",
                data = "Success!"
            )
        )
    }

    fun getClosedIdeaBoxes(): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val closedIdeaBoxes = currentEntityManager.createQuery(
            "SELECT ib FROM IdeaBox ib WHERE ib.isSclosed = true",
            IdeaBox::class.java
        ).resultList

        return ResponseEntity.ok(
            WebResponse<MutableList<IdeaBoxDto>>(
                code = HttpStatus.OK.value(),
                message = "IdeaBoxes",
                data = ideaBoxMapper.modelListToDto(closedIdeaBoxes)
            )
        )
    }

    private fun localDateToDate(localDate: LocalDate): Date {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    }
}