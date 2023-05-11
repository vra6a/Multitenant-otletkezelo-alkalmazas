package com.moa.backend.mapper

import com.moa.backend.model.*
import com.moa.backend.model.dto.IdeaDto
import com.moa.backend.model.slim.*
import com.moa.backend.repository.IdeaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class IdeaMapper: Mapper<IdeaDto, IdeaSlimDto, Idea> {

    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var tagMapper: TagMapper
    @Autowired
    lateinit var ideaBoxMapper: IdeaBoxMapper
    @Autowired
    lateinit var scoreMapper: ScoreMapper
    @Autowired
    lateinit var commentMapper: CommentMapper
    @Autowired
    lateinit var ideaRepository: IdeaRepository

    override fun modelToDto(entity: Idea): IdeaDto {

        val scores: MutableList<ScoreSlimDto> = emptyList<ScoreSlimDto>().toMutableList()
        entity.score.forEach{ score: Score ->
            scores.add(scoreMapper.modelToSlimDto(score))
        }
        val tags: MutableList<TagSlimDto> = emptyList<TagSlimDto>().toMutableList()
        entity.tags?.forEach{ tag: Tag ->
            tags.add(tagMapper.modelToSlimDto(tag))
        }
        val comments: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()
        entity.comments?.forEach{ comment: Comment ->
            comments.add(commentMapper.modelToSlimDto(comment))
        }
        val likes: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        entity.likes?.forEach{ user: User ->
            likes.add(userMapper.modelToSlimDto(user))
        }


        return IdeaDto(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            score = scores,
            owner = userMapper.modelToSlimDto(entity.owner),
            status = entity.status,
            creationDate = entity.creationDate,
            tags = tags,
            comments = comments,
            ideaBox = ideaBoxMapper.modelToSlimDto(entity.ideaBox),
            likes = likes
        )
    }

    override fun modelToSlimDto(entity: Idea): IdeaSlimDto {
        return IdeaSlimDto(
            id = entity.id,
            status = entity.status,
            title = entity.title
        )
    }

    override fun dtoToModel(domain: IdeaDto): Idea {
        val tags: MutableList<Tag> = emptyList<Tag>().toMutableList()
        domain.tags?.forEach{ tag: TagSlimDto ->
            tags.add(tagMapper.slimDtoToModel(tag))
        }
        if(domain.id == 0L) {
            return Idea(
                    id = domain .id,
                    title = domain.title,
                    description = domain.description,
                    score = emptyList<Score>().toMutableList(),
                    owner = userMapper.slimDtoToModel(domain.owner),
                    status = Status.SUBMITTED,
                    creationDate = Date(),
                    tags = tags,
                    comments = emptyList<Comment>().toMutableList(),
                    ideaBox = ideaBoxMapper.slimDtoToModel(domain.ideaBox),
                    likes = emptyList<User>().toMutableList(),
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: IdeaSlimDto): Idea {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): Idea {
        val idea = ideaRepository.findById(id).orElse(null)

        return  Idea(
            id = idea.id,
            title = idea.title,
            description = idea.description,
            score = idea.score,
            owner = idea.owner,
            status = idea.status,
            creationDate = idea.creationDate,
            tags = idea.tags,
            comments = idea.comments,
            ideaBox = idea.ideaBox,
            likes = idea.likes
        )
    }
}