package com.moa.backend.mapper

import com.moa.backend.model.*
import com.moa.backend.model.dto.UserDto
import com.moa.backend.model.slim.CommentSlimDto
import com.moa.backend.model.slim.IdeaBoxSlimDto
import com.moa.backend.model.slim.IdeaSlimDto
import com.moa.backend.model.slim.UserSlimDto
import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserMapper: Mapper<UserDto, UserSlimDto, User, > {

    @Autowired
    lateinit var commentMapper: CommentMapper
    @Autowired
    lateinit var ideaBoxMapper: IdeaBoxMapper
    @Autowired
    lateinit var ideaMapper: IdeaMapper
    @Autowired
    lateinit var userRepository: UserRepository

    override fun modelToDto(entity: User): UserDto {

        val likedComments: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()
        entity.likedComments?.forEach{ comment: Comment ->
            likedComments.add(commentMapper.modelToSlimDto(comment))
        }

        val comments: MutableList<CommentSlimDto> = emptyList<CommentSlimDto>().toMutableList()
        entity.comments?.forEach{ comment: Comment ->
            comments.add(commentMapper.modelToSlimDto(comment))
        }

        val ideaBoxes: MutableList<IdeaBoxSlimDto> = emptyList<IdeaBoxSlimDto>().toMutableList()
        entity.ideaBoxes?.forEach{ ideaBox: IdeaBox ->
            ideaBoxes.add(ideaBoxMapper.modelToSlimDto(ideaBox))
        }

        val ideas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.ideas?.forEach{ idea: Idea ->
            ideas.add(ideaMapper.modelToSlimDto(idea))
        }

        val likedIdeas: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.likedIdeas?.forEach{ idea: Idea ->
            likedIdeas.add(ideaMapper.modelToSlimDto(idea))
        }

        val itj: MutableList<IdeaSlimDto> = emptyList<IdeaSlimDto>().toMutableList()
        entity.ideasToJury?.forEach{ idea: Idea ->
            itj.add(ideaMapper.modelToSlimDto(idea))
        }

        return UserDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            role = entity.role,
            likedComments = likedComments,
            comments = comments,
            ideaBoxes = ideaBoxes,
            ideas = ideas,
            likedIdeas = likedIdeas,
            ideasToJury = itj,
        )
    }

    override fun modelToSlimDto(entity: User): UserSlimDto {
        return UserSlimDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            role = entity.role
        )
    }

    override fun dtoToModel(domain: UserDto): User {
        if(domain.id == 0L) {
            return User(
                    id = domain.id,
                    firstName = domain.firstName,
                    lastName = domain.lastName,
                    email = domain.email,
                    role = domain.role,
                    password = "",
                    likedIdeas = emptyList<Idea>().toMutableList(),
                    likedComments = emptyList<Comment>().toMutableList(),
                    ideas = emptyList<Idea>().toMutableList(),
                    ideaBoxes = emptyList<IdeaBox>().toMutableList(),
                    comments = emptyList<Comment>().toMutableList(),
                    ideasToJury = emptyList<Idea>().toMutableList(),
                    requiredToJury = emptyList<IdeaBox>().toMutableList(),
                    scoreSheets = emptyList<ScoreSheet>().toMutableList()
            )
        }
        return idToModel(domain.id)
    }

    override fun slimDtoToModel(domain: UserSlimDto): User {
        return idToModel(domain.id)
    }

    fun ModelListToSlimDto(modelList: List<User>): MutableList<UserSlimDto> {
        val list: MutableList<UserSlimDto> = emptyList<UserSlimDto>().toMutableList()
        modelList.forEach { model ->
            list.add(modelToSlimDto(model))
        }
        return list
    }

    private fun idToModel(id: Long): User {
        val user = userRepository.findById(id).orElse(null)

        return User(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password,
            role = user.role,
            likedIdeas = user.likedIdeas,
            likedComments = user.likedComments,
            ideas = user.ideas,
            ideaBoxes = user.ideaBoxes,
            comments = user.comments,
            ideasToJury = user.ideasToJury,
            requiredToJury = user.requiredToJury,
            scoreSheets = user.scoreSheets,
        )
    }
}