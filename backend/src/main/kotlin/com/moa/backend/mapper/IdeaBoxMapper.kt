package com.moa.backend.mapper

import com.moa.backend.model.Idea
import com.moa.backend.model.IdeaBox
import com.moa.backend.model.ScoreSheet
import com.moa.backend.model.User
import com.moa.backend.model.dto.IdeaBoxDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.ScoreSheetSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.multitenancy.TenantContext
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
    @Autowired
    lateinit var scoreSheetMapper: ScoreSheetMapper


    override fun modelToDto(entity: IdeaBox): IdeaBoxDto {

        val ideas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.ideas.forEach{ idea: Idea ->
            ideas.add(ideaMapper.modelToSlimDto(idea))
        }

        val requiredJuries: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        entity.defaultRequiredJuries?.forEach { jury ->
            requiredJuries.add(userMapper.modelToSlimDto(jury))
        }

        val scoreSheetTemplates: MutableList<ScoreSheetSlimDto> = emptyList<ScoreSheetSlimDto>().toMutableList()
        entity.scoreSheetTemplates.forEach{ scoreSheet ->
            scoreSheetTemplates.add(scoreSheetMapper.modelToSlimDto(scoreSheet))
        }


        return IdeaBoxDto(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            startDate = entity.startDate,
            endDate = entity.endDate,
            creator = userMapper.modelToSlimDto(entity.creator),
            ideas = ideas,
            defaultRequiredJuries = requiredJuries,
            scoreSheetTemplates = scoreSheetTemplates,
            isSclosed = entity.isSclosed,
        )
    }

    override fun modelToSlimDto(entity: IdeaBox): IdeaBoxSlimDto {
        return IdeaBoxSlimDto(
            id = entity.id,
            name = entity.name,
            startDate = entity.startDate,
            endDate = entity.endDate,
            draft = entity.scoreSheetTemplates.isEmpty(),
            isSclosed = entity.isSclosed,
        )
    }

    override fun dtoToModel(domain: IdeaBoxDto): IdeaBox {
        if(domain.id == 0L) {
            val defaultRequiredJuries: MutableList<User> = emptyList<User>().toMutableList()
            domain.defaultRequiredJuries?.forEach{ jury ->
                defaultRequiredJuries.add(userMapper.slimDtoToModel(jury))
            }

            val scoreSheetTemplates: MutableList<ScoreSheet> = emptyList<ScoreSheet>().toMutableList()
            domain.scoreSheetTemplates?.forEach{ scoreSheet ->
                scoreSheetTemplates.add(scoreSheetMapper.slimDtoToModel(scoreSheet))
            }
            val currentTenant = TenantContext.getCurrentTenant().orEmpty()
            return IdeaBox(
                    id = domain.id,
                    name = domain.name,
                    description = domain.description,
                    startDate = domain.startDate,
                    endDate = domain.endDate,
                    creator = userMapper.slimDtoToModel(domain.creator),
                    ideas = emptyList<Idea>().toMutableList(),
                    defaultRequiredJuries = defaultRequiredJuries,
                    scoreSheetTemplates = scoreSheetTemplates,
                    isSclosed = domain.isSclosed,
                    tenantId = currentTenant
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: IdeaBoxSlimDto): IdeaBox {
        return idToModel(domain.id)
    }

    fun ModelListToSlimDto(modelList: List<IdeaBox>): MutableList<IdeaBoxSlimDto> {
        val list: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()
        modelList.forEach { model ->
            list.add(modelToSlimDto(model))
        }
        return list
    }

    fun modelListToDto(modelList: List<IdeaBox>): MutableList<IdeaBoxDto> {
        val list: MutableList<IdeaBoxDto> = emptyList<IdeaBoxDto>().toMutableList()
        modelList.forEach { model ->
            list.add(modelToDto(model))
        }
        return list
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
            defaultRequiredJuries = ideaBox.defaultRequiredJuries,
            scoreSheetTemplates = ideaBox.scoreSheetTemplates,
            isSclosed = ideaBox.isSclosed,
            tenantId = ideaBox.tenantId
        )
    }
}