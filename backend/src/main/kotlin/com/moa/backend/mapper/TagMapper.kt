package com.moa.backend.mapper

import com.moa.backend.model.Idea
import com.moa.backend.model.Tag
import com.moa.backend.model.dto.TagDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.TagSlimDto
import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.repository.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagMapper: Mapper<TagDto, TagSlimDto, Tag> {

    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var tagRepository: TagRepository


    override fun modelToDto(entity: Tag): TagDto {

        val ideas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.taggedIdeas?.forEach{ idea: Idea ->
            ideas.add(ideaMapper.modelToSlimDto(idea))
        }


        return TagDto(
            id = entity.id,
            name = entity.name,
            taggedIdeas = ideas
        )
    }

    override fun modelToSlimDto(entity: Tag): TagSlimDto {
        return TagSlimDto(
            id = entity.id,
            name = entity.name
        )
    }

    override fun dtoToModel(domain: TagDto): Tag {
        if(domain.id == 0L) {
            val currentTenant = TenantContext.getCurrentTenant().orEmpty()
            return Tag(
                id = domain.id,
                name = domain.name,
                taggedIdeas = emptyList<Idea>().toMutableList(),
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: TagSlimDto): Tag {
        return idToModel(domain.id)
    }

    private fun idToModel(id: Long): Tag {
        val tag = tagRepository.findById(id).orElse(null)

        return Tag(
            id = tag.id,
            name = tag.name,
            taggedIdeas = tag.taggedIdeas,
        )
    }
}