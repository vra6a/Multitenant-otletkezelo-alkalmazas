package com.moa.backend.Mappers

import TestUtility
import com.moa.backend.mapper.*
import com.moa.backend.repository.UserRepository
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class UserMapperTest {
    private lateinit var commentMapper: CommentMapper
    private lateinit var ideaBoxMapper: IdeaBoxMapper
    private lateinit var ideaMapper: IdeaMapper
    private lateinit var userMapper: UserMapper
    private lateinit var userRepository: UserRepository
    private lateinit var testUtility: TestUtility

    @BeforeEach
    fun setup() {
        commentMapper = mockk()
        ideaBoxMapper = mockk()
        ideaMapper = mockk()
        userRepository = mockk()
        testUtility = TestUtility()

        userMapper = UserMapper().apply {
            this.commentMapper = this@UserMapperTest.commentMapper
            this.ideaBoxMapper = this@UserMapperTest.ideaBoxMapper
            this.ideaMapper = this@UserMapperTest.ideaMapper
            this.userRepository = this@UserMapperTest.userRepository
        }
    }

    @Test
    fun `should return UserDto from User (modelToDto)`() {
        val user = testUtility.createMockUser()
        val expectedUserDto = testUtility.createMockUserDto()

        val userDto = userMapper.modelToDto(user)

        Assertions.assertEquals(expectedUserDto, userDto)
    }

    @Test
    fun `should return UserSlimDto from User (modelToSlimDto)`() {
        val user = testUtility.createMockUser()
        val expectedUserSlimDto = testUtility.createMockSlimUserDto()

        val userDto = userMapper.modelToSlimDto(user)

        Assertions.assertEquals(expectedUserSlimDto, userDto)
    }

    @Test
    fun `should return User from UserDto (dtoToModel)`() {
        val userDto = testUtility.createMockUserDto()
        val expectedUser = testUtility.createMockUser()

        every { userRepository.findById(any()) } returns Optional.of(expectedUser)

        val user = userMapper.dtoToModel(userDto)

        Assertions.assertEquals(expectedUser, user)
    }

    @Test
    fun `should return User from UserDto (id=0L) (dtoToModel)`() {
        val userDto = testUtility.createMockUserDto()
        userDto.id = 0L
        val expectedUser = testUtility.createMockUser()
        expectedUser.id = 0L


        every { userRepository.findById(any()) } returns Optional.of(expectedUser)

        val user = userMapper.dtoToModel(userDto)

        Assertions.assertEquals(expectedUser.id, user.id)
    }

    @Test
    fun `should return User from UserSlimDto (slimDtoToModel)`() {
        val userSlimDto = testUtility.createMockSlimUserDto()
        val expectedUser = testUtility.createMockUser()

        every { userRepository.findById(any()) } returns Optional.of(expectedUser)

        val user = userMapper.slimDtoToModel(userSlimDto)

        Assertions.assertEquals(expectedUser, user)
    }

}