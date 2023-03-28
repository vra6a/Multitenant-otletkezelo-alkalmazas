package com.moa.backend.service

import com.moa.backend.model.Role
import com.moa.backend.model.ScoreType
import com.moa.backend.model.Status
import org.springframework.stereotype.Service

@Service
class UtilityService {

    fun getRoles(): Array<Role> {
        return Role.values()
    }

    fun getStatuses(): Array<Status> {
        return Status.values()
    }

    fun getScoreTypes(): Array<ScoreType> {
        return ScoreType.values()
    }
}