import com.moa.backend.model.*
import com.moa.backend.model.dto.*
import com.moa.backend.model.slim.*
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TestUtility () {
    fun createMockUser(): User {
        return User(
            id = 1,
            firstName = "test",
            lastName = "user",
            email = "user@test.com",
            password = "test1",
            role = Role.ADMIN,
            likedIdeas = mutableListOf(),
            likedComments = mutableListOf(),
            ideas = mutableListOf(),
            ideaBoxes = mutableListOf(),
            ideasToJury = mutableListOf(),
            requiredToJury = mutableListOf(),
            scoreSheets = mutableListOf(),
            comments = mutableListOf(),
        )
    }

    fun createMockUserDto(): UserDto {
        return UserDto(
            id = 1,
            firstName = "test",
            lastName = "user",
            email = "user@test.com",
            role = Role.ADMIN,
            likedIdeas = mutableListOf(),
            likedComments = mutableListOf(),
            ideas = mutableListOf(),
            ideaBoxes = mutableListOf(),
            ideasToJury = mutableListOf(),
            comments = mutableListOf()
        )
    }

    fun createMockSlimUserDto(): UserSlimDto {
        return UserSlimDto(
            id = 1,
            firstName = "test",
            lastName = "user",
            email = "user@test.com",
            role = Role.ADMIN,
        )
    }

    fun createMockTag(): Tag {
        return Tag(
            id = 1,
            name = "Tag1",
            taggedIdeas = mutableListOf()
        )
    }

    fun createMockTagDto(): TagDto {
        return TagDto(
            id = 1,
            name = "Tag1",
            taggedIdeas = mutableListOf()
        )
    }

    fun createMockSlimTagDto(): TagSlimDto {
        return TagSlimDto(
            id = 1,
            name = "Tag1",
        )
    }

    fun createMockScoreItem(): ScoreItem {
        return ScoreItem(
            id = 1,
            type = ScoreType.SLIDER,
            scoreSheet = createMockScoreSheet(),
            title = "ScoreSheetTitle",
            score = 6,
            text = "ScoreSheetText"
        )
    }

    fun createMockScoreItemDto(): ScoreItemDto {
        return ScoreItemDto(
            id = 1,
            type = ScoreType.SLIDER,
            scoreSheet = createMockSlimScoreSheetDto(),
            title = "ScoreSheetTitle",
            score = 6,
            text = "ScoreSheetText"
        )
    }

    fun createMockSlimScoreItemDto(): ScoreItemSlimDto {
        return ScoreItemSlimDto(
            id = 1,
            type = ScoreType.SLIDER,
            scoreSheet = createMockSlimScoreSheetDto(),
            title = "ScoreSheetTitle",
            score = 6,
            text = "ScoreSheetText"
        )
    }


    fun createMockComment(): Comment {
        return Comment(
            id = 1,
            creationDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            text = "testString",
            owner = createMockUser(),
            idea = createMockIdea(),
            isEdited = false,
            likes = mutableListOf(),
        )
    }

    fun createMockCommentDto(): CommentDto {
        return CommentDto(
            id = 1,
            creationDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            text = "testString",
            owner = createMockSlimUserDto(),
            idea = createMockSlimIdeaDto(),
            likes = mutableListOf(),
            isEdited = false,
        )
    }

    fun createMockSlimCommentDto(): CommentSlimDto {
        return CommentSlimDto(
            id = 1,
            creationDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            text = "testString",
            owner = createMockSlimUserDto()
        )
    }

    fun createMockIdea(): Idea {
        return Idea(
            id = 1,
            title = "TestIdeaTitle",
            description = "testIdeaDescription",
            owner = createMockUser(),
            status = Status.SUBMITTED,
            creationDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            tags = mutableListOf(),
            requiredJuries = mutableListOf(),
            comments = mutableListOf(),
            ideaBox = createMockIdeaBox(),
            likes = mutableListOf(),
            scoreSheets = mutableListOf(),
        )
    }

    fun createMockIdeaDto(): IdeaDto {
        return IdeaDto(
            id = 1,
            title = "TestIdeaTitle",
            description = "testIdeaDescription",
            owner = createMockSlimUserDto(),
            status = Status.SUBMITTED,
            creationDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            tags = mutableListOf(),
            comments = mutableListOf(),
            ideaBox = createMockSlimIdeaBoxDto(),
            likes = mutableListOf(),
            requiredJuries = mutableListOf(),
            scoreSheets = mutableListOf(),
        )
    }

    fun createMockSlimIdeaDto(): IdeaSlimDto {
        return IdeaSlimDto(
            id = 1,
            title = "TestIdeaTitle",
            status = Status.SUBMITTED
        )
    }

    fun createMockIdeaBox(): IdeaBox {
        return IdeaBox(
            id = 1,
            name = "IdeaBox1Test",
            description = "testDescription",
            startDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            endDate = Date.from((LocalDate.of(2024, 1, 2)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            creator = createMockUser(),
            ideas = mutableListOf(),
            defaultRequiredJuries = mutableListOf(),
            scoreSheetTemplates = mutableListOf(),
            isSclosed = false
        )
    }

    fun createMockIdeaBoxDto(): IdeaBoxDto {
        return IdeaBoxDto(
            id = 1,
            name = "IdeaBox1Test",
            description = "testDescription",
            startDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            endDate = Date.from((LocalDate.of(2024, 1, 2)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            creator = createMockSlimUserDto(),
            ideas = mutableListOf(),
            defaultRequiredJuries = mutableListOf(),
            scoreSheetTemplates = mutableListOf(),
            isSclosed = false
            )
    }

    fun createMockSlimIdeaBoxDto(): IdeaBoxSlimDto {
        return IdeaBoxSlimDto(
            id = 1,
            name = "IdeaBox1Test",
            startDate = Date.from((LocalDate.of(2024, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            endDate = Date.from((LocalDate.of(2024, 1, 2)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
            draft = true,
            isSclosed = false
        )
    }

    fun createMockScoreSheet(): ScoreSheet {
        return ScoreSheet(
            id = 1,
            scores = mutableListOf(),
            owner = createMockUser(),
            idea = createMockIdea(),
            templateFor = createMockIdeaBox(),
        )
    }

    fun createMockScoreSheetDto(): ScoreSheetDto {
        return ScoreSheetDto(
            id = 1,
            scores = mutableListOf(),
            owner = createMockSlimUserDto(),
            idea = createMockSlimIdeaDto(),
            templateFor = createMockSlimIdeaBoxDto(),
        )
    }

    fun createMockSlimScoreSheetDto(): ScoreSheetSlimDto {
        return ScoreSheetSlimDto(
            id = 1,
            owner = createMockSlimUserDto(),
            idea = createMockSlimIdeaDto()
        )
    }
}
