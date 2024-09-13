package com.moa.backend.mapper

import com.moa.backend.model.ScoreItem
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.dto.ScoreItemDto
import com.moa.backend.model.dto.ScoreSheetDto
import com.moa.backend.model.slim.ScoreItemSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.repository.IdeaBoxRepository
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
    lateinit var ideaBoxRepository: IdeaBoxRepository
    @Autowired
    lateinit var scoreItemMapper: ScoreItemMapper

    override fun modelToDto(entity: ScoreSheet): ScoreSheetDto {

        val scores: MutableList<ScoreItemDto> = emptyList<ScoreItemDto>().toMutableList()
        entity.scores?.forEach { score: ScoreItem ->
            scores.add(scoreItemMapper.modelToDto(score))
        }

        return ScoreSheetDto(
            id = entity.id,
            idea = entity.idea?.let { ideaMapper.modelToSlimDto(it) },
            owner = userMapper.modelToSlimDto(entity.owner),
            scores = scores,
            templateFor = entity.templateFor?.let { ideaBoxMapper.modelToSlimDto(it) }
        )
    }

    override fun modelToSlimDto(entity: ScoreSheet): ScoreSheetSlimDto {
        return ScoreSheetSlimDto(
            id = entity.id,
            idea = entity.idea?.let { ideaMapper.modelToSlimDto(it) },
            owner = userMapper.modelToSlimDto(entity.owner),
        )
    }

    fun initializeScoreSheet(ss: ScoreSheetDto): ScoreSheet {

        return ScoreSheet(
            id = ss.id,
            idea = null,
            owner = userMapper.slimDtoToModel(ss.owner),
            scores = null,
            templateFor = ss.templateFor?.let { ideaBoxMapper.slimDtoToModel(it) }
        )
    }

    override fun dtoToModel(domain: ScoreSheetDto): ScoreSheet {
        if(domain.id == 0L) {
            val scores: MutableList<ScoreItem> = emptyList<ScoreItem>().toMutableList()
            domain.scores?.forEach{ scoreItem: ScoreItemDto ->
                scores.add(scoreItemMapper.dtoToModel(scoreItem))
            }

            return ScoreSheet(
                id = domain.id,
                idea = domain.idea?.let { ideaMapper.slimDtoToModel(it) },
                owner = userMapper.slimDtoToModel(domain.owner),
                scores = scores,
                templateFor = domain.templateFor?.let { ideaBoxMapper.slimDtoToModel(it) }
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: ScoreSheetSlimDto): ScoreSheet {
        return idToModel(domain.id)
    }

    fun modelListToDto(modelList: List<ScoreSheet>): MutableList<ScoreSheetDto> {
        val list: MutableList<ScoreSheetDto> = emptyList<ScoreSheetDto>().toMutableList()
        modelList.forEach { model ->
            list.add(modelToDto(model))
        }
        return list
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