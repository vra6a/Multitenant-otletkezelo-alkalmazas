package com.moa.backend.mapper

import com.moa.backend.model.ScoreItem
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.ScoreItemRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScoreItemMapper: Mapper<ScoreItemDto, ScoreItemSlimDto, ScoreItem> {

    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper
    @Autowired
    lateinit var scoreItemRepository: ScoreItemRepository

    override fun modelToDto(entity: ScoreItem): ScoreItemDto {
        return ScoreItemDto(
            id = entity.id,
            score = entity.score,
            scoreSheet = scoreSheetMapper.modelToSlimDto(entity.scoreSheet),
            text = entity.text,
            title = entity.title,
            type = entity.type
        )
    }

    override fun modelToSlimDto(entity: ScoreItem): ScoreItemSlimDto {
        return ScoreItemSlimDto(
            id = entity.id,
            score = entity.score,
            scoreSheet = scoreSheetMapper.modelToSlimDto(entity.scoreSheet),
            text = entity.text,
            title = entity.title,
            type = entity.type
        )
    }

    override fun dtoToModel(domain: ScoreItemDto): ScoreItem {
        if(domain.id == 0L) {
            val currentTenant = TenantContext.getCurrentTenant().orEmpty()
            return ScoreItem(
                id = domain.id,
                score = domain.score,
                scoreSheet = scoreSheetMapper.slimDtoToModel(domain.scoreSheet),
                text = domain.text,
                title = domain.title,
                type = domain.type,
                tenantId = currentTenant
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: ScoreItemSlimDto): ScoreItem {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): ScoreItem {
        val scoreItem = scoreItemRepository.findById(id).orElse(null)

        return ScoreItem(
            id = scoreItem.id,
            scoreSheet = scoreItem.scoreSheet,
            score = scoreItem.score,
            text = scoreItem.text,
            title = scoreItem.title,
            type = scoreItem.type,
            tenantId = scoreItem.tenantId
        )
    }

}