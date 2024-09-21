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
    lateinit var commentMapper: CommentMapper
    @Autowired
    lateinit var ideaRepository: IdeaRepository
    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper

    override fun modelToDto(entity: Idea): IdeaDto {

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
        val juries: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        entity.requiredJuries?.forEach{ user: User ->
            juries.add(userMapper.modelToSlimDto(user))
        }

        val scoreSheets: MutableList<ScoreSheetSlimDto> = emptyList<ScoreSheetSlimDto>().toMutableList()
        entity.scoreSheets.forEach{ scoreSheet ->
            scoreSheets.add(scoreSheetMapper.modelToSlimDto(scoreSheet))
        }

        return IdeaDto(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            owner = userMapper.modelToSlimDto(entity.owner),
            status = entity.status,
            creationDate = entity.creationDate,
            tags = tags,
            comments = comments,
            ideaBox = ideaBoxMapper.modelToSlimDto(entity.ideaBox),
            likes = likes,
            requiredJuries = juries,
            scoreSheets = scoreSheets
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

        val juries: MutableList<User> = emptyList<User>().toMutableList()
        domain.requiredJuries?.forEach{ user: UserSlimDto ->
            juries.add(userMapper.slimDtoToModel(user))
        }

        val scoreSheets: MutableList<ScoreSheet> = emptyList<ScoreSheet>().toMutableList()
        domain.scoreSheets?.forEach{ scoreSheet: ScoreSheetSlimDto ->
            scoreSheets.add(scoreSheetMapper.slimDtoToModel(scoreSheet))
        }

        if(domain.id == 0L) {
            return Idea(
                    id = domain .id,
                    title = domain.title,
                    description = domain.description,
                    owner = userMapper.slimDtoToModel(domain.owner),
                    status = Status.SUBMITTED,
                    creationDate = Date(),
                    tags = tags,
                    comments = emptyList<Comment>().toMutableList(),
                    ideaBox = ideaBoxMapper.slimDtoToModel(domain.ideaBox),
                    likes = emptyList<User>().toMutableList(),
                    requiredJuries = juries,
                    scoreSheets = scoreSheets,
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: IdeaSlimDto): Idea {
        return idToModel(domain.id)
    }

    fun ModelListToSlimDto(modelList: List<Idea>): MutableList<IdeaSlimDto> {
        val list: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        modelList.forEach { model ->
            list.add(modelToSlimDto(model))
        }
        return list
    }

    fun idToModel(id: Long): Idea {
        val idea = ideaRepository.findById(id).orElse(null)

        return  Idea(
            id = idea.id,
            title = idea.title,
            description = idea.description,
            owner = idea.owner,
            status = idea.status,
            creationDate = idea.creationDate,
            tags = idea.tags,
            comments = idea.comments,
            ideaBox = idea.ideaBox,
            likes = idea.likes,
            requiredJuries = idea.requiredJuries,
            scoreSheets = idea.scoreSheets,
        )
    }
}