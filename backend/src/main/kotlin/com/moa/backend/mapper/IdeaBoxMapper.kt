package com.moa.backend.mapper

import com.moa.backend.model.Idea
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.User
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.IdeaBoxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IdeaBoxMapper: Mapper<IdeaBoxDto, IdeaBoxSlimDto, IdeaBox> {

    @Autowired
    lateinit var userMapper: UserMapper
    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var ideaBoxRepository: IdeaBoxRepository


    override fun modelToDto(entity: IdeaBox): IdeaBoxDto {

        val ideas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.ideas.forEach{ idea: Idea ->
            ideas.add(ideaMapper.modelToSlimDto(idea))
        }

        val requiredJuries: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        entity.defaultRequiredJuries?.forEach { jury ->
            requiredJuries.add(userMapper.modelToSlimDto(jury))
        }

        return IdeaBoxDto(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            startDate = entity.startDate,
            endDate = entity.endDate,
            creator = userMapper.modelToSlimDto(entity.creator),
            ideas = ideas,
            requiredJuries = requiredJuries,
        )
    }

    override fun modelToSlimDto(entity: IdeaBox): IdeaBoxSlimDto {
        return IdeaBoxSlimDto(
            id = entity.id,
            name = entity.name,
            startDate = entity.startDate,
            endDate = entity.endDate
        )
    }

    override fun dtoToModel(domain: IdeaBoxDto): IdeaBox {
        if(domain.id == 0L) {
            val defaultRequiredJuries: MutableList<User> = emptyList<User>().toMutableList()
            domain.requiredJuries?.forEach{ jury ->
                defaultRequiredJuries.add(userMapper.slimDtoToModel(jury))
            }
            return IdeaBox(
                    id = domain.id,
                    name = domain.name,
                    description = domain.description,
                    startDate = domain.startDate,
                    endDate = domain.endDate,
                    creator = userMapper.slimDtoToModel(domain.creator),
                    ideas = emptyList<Idea>().toMutableList(),
                    defaultRequiredJuries = defaultRequiredJuries
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: IdeaBoxSlimDto): IdeaBox {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): IdeaBox {
        val ideaBox = ideaBoxRepository.findById(id).orElse(null)

        return IdeaBox(
            id = ideaBox.id,
            name = ideaBox.name,
            description = ideaBox.description,
            startDate = ideaBox.startDate,
            endDate = ideaBox.endDate,
            creator = ideaBox.creator,
            ideas = ideaBox.ideas,
            defaultRequiredJuries = ideaBox.defaultRequiredJuries
        )
    }
}