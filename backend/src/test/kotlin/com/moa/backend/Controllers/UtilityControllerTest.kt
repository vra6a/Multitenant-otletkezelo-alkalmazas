package com.moa.backend.Controllers

import com.moa.backend.controller.UtilityController
import com.moa.backend.model.Role
import com.moa.backend.model.ScoreType
import com.moa.backend.model.Status
import com.moa.backend.service.UtilityService
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@ExtendWith(MockKExtension::class)
class UtilityControllerTest {
    private lateinit var utilityController: UtilityController
    private lateinit var utilityService: UtilityService

    @BeforeEach
    fun setup() {
        utilityService = mockk()
        utilityController = UtilityController()
        utilityController.utilityService = utilityService
    }

    @Test
    fun `getRoles() should call utilityService getRoles()`() {
        every { utilityService.getRoles() } returns arrayOf(Role.ADMIN, Role.JURY, Role.USER)
        utilityController.getRoles()
        verify { utilityService.getRoles() }
    }

    @Test
    fun `getStatuses() should call utilityService getStatuses()`() {
        every { utilityService.getStatuses() } returns arrayOf(Status.DENIED, Status.REVIEWED, Status.APPROVED, Status.SUBMITTED)
        utilityController.getStatuses()
        verify { utilityService.getStatuses() }
    }

    @Test
    fun `getScoreTypes() should call utilityService getScoreTypes()`() {
        every { utilityService.getScoreTypes() } returns arrayOf(ScoreType.SLIDER, ScoreType.STAR)
        utilityController.getScoreTypes()
        verify { utilityService.getScoreTypes() }
    }
}