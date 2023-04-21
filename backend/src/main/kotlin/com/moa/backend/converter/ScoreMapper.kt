package com.moa.backend.converter

import com.moa.backend.model.Score
import com.moa.backend.model.Tag
import com.moa.backend.model.dto.ScoreDto
import com.moa.backend.model.slim.ScoreSlimDto
import com.moa.backend.repository.ScoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ScoreMapper: Mapper<ScoreDto, ScoreSlimDto, Score> {

    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var scoreRepository: ScoreRepository


    override fun modelToDto(entity: Score): ScoreDto {
        return ScoreDto(
            id = entity.id,
            score = entity.score,
            type = entity.type,
            idea = ideaMapper.modelToSlimDto(entity.idea)
        )
    }

    override fun modelToSlimDto(entity: Score): ScoreSlimDto {
        return ScoreSlimDto(
            id = entity.id,
            score = entity.score,
            type = entity.type
        )
    }

    override fun dtoToModel(domain: ScoreDto): Score {
        if(domain.id == 0L) {
            return Score(
                    id = domain.id,
                    score = domain.score,
                    type = domain.type,
                    idea = ideaMapper.slimDtoToModel(domain.idea)
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: ScoreSlimDto): Score {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): Score {
        val score = scoreRepository.findById(id).orElse(null)

        return Score(
            id = score.id,
            score = score.score,
            type = score.type,
            idea = score.idea
        )
    }
}