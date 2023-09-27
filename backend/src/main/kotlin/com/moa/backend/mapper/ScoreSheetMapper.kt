package com.moa.backend.mapper

import com.moa.backend.model.ScoreItem
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.ScoreSheetRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScoreSheetMapper: Mapper<ScoreSheetDto, ScoreSheetSlimDto, ScoreSheet> {

    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var ideaBoxMapper: IdeaBoxMapper
    @Autowired
    lateinit var scoreSheetRepository: ScoreSheetRepository
    @Autowired
    lateinit var scoreItemMapper: ScoreItemMapper

    override fun modelToDto(entity: ScoreSheet): ScoreSheetDto {

        val scores: MutableList<ScoreItemSlimDto> = emptyList<ScoreItemSlimDto>().toMutableList()
        entity.scores?.forEach { score: ScoreItem ->
            scores.add(scoreItemMapper.modelToSlimDto(score))
        }

        return ScoreSheetDto(
            id = entity.id,
            idea = ideaMapper.modelToSlimDto(entity.idea),
            owner = userMapper.modelToSlimDto(entity.owner),
            scores = scores,
            templateFor = ideaBoxMapper.modelToSlimDto(entity.templateFor)
        )
    }

    override fun modelToSlimDto(entity: ScoreSheet): ScoreSheetSlimDto {
        return ScoreSheetSlimDto(
            id = entity.id,
            idea = ideaMapper.modelToSlimDto(entity.idea),
            owner = userMapper.modelToSlimDto(entity.owner),
        )
    }

    override fun dtoToModel(domain: ScoreSheetDto): ScoreSheet {
        if(domain.id == 0L) {
            val scores: MutableList<ScoreItem> = emptyList<ScoreItem>().toMutableList()
            domain.scores.forEach{ scoreItem: ScoreItemSlimDto ->
                scores.add(scoreItemMapper.slimDtoToModel(scoreItem))
            }

            return ScoreSheet(
                id = domain.id,
                idea = ideaMapper.slimDtoToModel(domain.idea),
                owner = userMapper.slimDtoToModel(domain.owner),
                scores = scores,
                templateFor = ideaBoxMapper.slimDtoToModel(domain.templateFor)
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: ScoreSheetSlimDto): ScoreSheet {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): ScoreSheet {
        val scoreSheet = scoreSheetRepository.findById(id).orElse(null)

        return ScoreSheet(
            id = scoreSheet.id,
            idea = scoreSheet.idea,
            owner = scoreSheet.owner,
            scores = scoreSheet.scores,
            templateFor = scoreSheet.templateFor
        )
    }
}