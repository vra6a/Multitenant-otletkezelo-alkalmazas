package com.moa.backend.service

import com.moa.backend.mapper.IdeaMapper
import com.moa.backend.mapper.ScoreItemMapper
import com.moa.backend.mapper.ScoreSheetMapper
import com.moa.backend.model.Idea
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.Status
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.IdeaRepository
import com.moa.backend.repository.ScoreItemRepository
import com.moa.backend.repository.ScoreSheetRepository
import com.moa.backend.repository.UserRepository
import com.moa.backend.utility.WebResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class ScoreItemService {

    @Autowired
    lateinit var scoreItemRepository: ScoreItemRepository

    @Autowired
    lateinit var scoreSheetRepository: ScoreSheetRepository

    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper

    @Autowired
    lateinit var scoreItemMapper: ScoreItemMapper

    @Autowired
    lateinit var ideaRepository: IdeaRepository

    @Autowired
    lateinit var ideaMapper: IdeaMapper

    @Autowired
    lateinit var userRepository: UserRepository

    private val logger = KotlinLogging.logger {}

    @PersistenceContext
    lateinit var entityManager: EntityManager

    fun AddScoreItemToScoreSheetTemplate(id: Long, scoreItem: ScoreItemSlimDto): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val scoreSheetTemplate = currentEntityManager.createQuery(
            "SELECT s FROM ScoreSheet s WHERE s.id = :id", ScoreSheet::class.java
        )
            .setParameter("id", id)
            .resultList
            .firstOrNull()

        if (scoreSheetTemplate == null) {
            logger.info { "MOA-INFO: ScoreSheet with id: $id not found." }
            return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find ScoreSheet with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )
        }

        if (scoreSheetTemplate.scores == null) {
            scoreSheetTemplate.scores = mutableListOf()
        }

        val newScoreItem = scoreItemMapper.slimDtoToModel(scoreItem)
        scoreSheetTemplate.scores!!.add(newScoreItem)

        currentEntityManager.merge(scoreSheetTemplate)

        logger.info { "MOA-INFO: Score item added to ScoreSheet with id: $id." }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Item added successfully",
                data = "Success"
            )
        )
    }

    fun CreateScoreItem(scoreItems: MutableList<ScoreItemDto>, id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        scoreItems.forEach { item: ScoreItemDto ->
            val scoreItem = scoreItemMapper.dtoToModel(item)
            currentEntityManager.persist(scoreItem)
        }

        return ResponseEntity.ok(
            WebResponse<String>(
                code = HttpStatus.OK.value(),
                message = "Items Added Successfully",
                data = "ok"
            )
        )
    }

    fun GetScoreSheetById(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val sheet = currentEntityManager.find(ScoreSheet::class.java, id)
            ?: return ResponseEntity(
                WebResponse<String>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Cannot find ScoreSheet with this id $id!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        return ResponseEntity.ok(
            WebResponse<ScoreSheetDto>(
                code = HttpStatus.OK.value(),
                message = "",
                data = scoreSheetMapper.modelToDto(sheet)
            )
        )
    }

    fun saveScoreSheet(scoreSheet: ScoreSheetDto, id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val authentication = SecurityContextHolder.getContext().authentication
        val user = userRepository.findByEmail(authentication.name).orElse(null)

        val idea = currentEntityManager.find(Idea::class.java, scoreSheet.idea!!.id)
            ?: return ResponseEntity(
                WebResponse<ScoreSheetDto>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Idea not found!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        if (idea.scoreSheets.any { it.owner.id == user?.id }) {
            return ResponseEntity.ok(
                WebResponse<ScoreSheetDto>(
                    code = HttpStatus.METHOD_NOT_ALLOWED.value(),
                    message = "Jury already scored this idea!",
                    data = null
                )
            )
        }

        val newScoreSheet = scoreSheet.copy(
            id = 0,
            scores = mutableListOf(),
            idea = ideaMapper.modelToSlimDto(idea),
            templateFor = null
        )

        val savedScoreSheet = scoreSheetRepository.save(scoreSheetMapper.dtoToModel(newScoreSheet))

        scoreSheet.scores?.forEach { score ->
            score.id = 0L
            score.scoreSheet = scoreSheetMapper.modelToSlimDto(savedScoreSheet)
            logger.info { "score: $score" }
            val savedScoreItem = scoreItemRepository.save(scoreItemMapper.dtoToModel(score))
            newScoreSheet.scores!!.add(scoreItemMapper.modelToDto(savedScoreItem))
        }

        idea.status = Status.REVIEWED
        currentEntityManager.merge(idea)

        return ResponseEntity.ok(
            WebResponse<ScoreSheetDto>(
                code = HttpStatus.OK.value(),
                message = "Scoring is saved successfully!",
                data = scoreSheet
            )
        )
    }

    fun getScoreSheetsByIdea(id: Long): ResponseEntity<*> {
        val currentEntityManager = TenantContext.getEntityManager()
            ?: throw IllegalStateException("EntityManager not found in TenantContext.")

        val idea = currentEntityManager.find(Idea::class.java, id)
            ?: return ResponseEntity(
                WebResponse<MutableList<ScoreSheetDto>>(
                    code = HttpStatus.NOT_FOUND.value(),
                    message = "Idea not found!",
                    data = null
                ),
                HttpStatus.NOT_FOUND
            )

        val scoreSheets = mutableListOf<ScoreSheetDto>()

        val template = idea.ideaBox.scoreSheetTemplates.firstOrNull()
        template?.let { scoreSheetTemplate ->
            scoreSheetTemplate.scores?.forEach { score ->
                score.score = 0
            }
            scoreSheets.add(scoreSheetMapper.modelToDto(scoreSheetTemplate))
        }

        idea.scoreSheets.forEach { ss ->
            scoreSheets.add(scoreSheetMapper.modelToDto(ss))
        }

        return ResponseEntity.ok(
            WebResponse<MutableList<ScoreSheetDto>>(
                code = HttpStatus.OK.value(),
                message = "",
                data = scoreSheets
            )
        )
    }
}