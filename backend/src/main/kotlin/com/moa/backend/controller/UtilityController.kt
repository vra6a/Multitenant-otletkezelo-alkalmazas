package com.moa.backend.controller

import com.moa.backend.model.Role
import com.moa.backend.model.ScoreType
import com.moa.backend.model.Status
import com.moa.backend.service.UtilityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UtilityController {

    @Autowired
    lateinit var utilityService: UtilityService

    @GetMapping("/roles")
    fun getRoles(): Array<Role> {
        return utilityService.getRoles()
    }

    @GetMapping("/statuses")
    fun getStatuses(): Array<Status> {
        return utilityService.getStatuses()
    }

    @GetMapping("/score-types")
    fun getScoreTypes(): Array<ScoreType> {
        return utilityService.getScoreTypes()
    }
}