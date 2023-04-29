package com.moa.backend.model.slim

import com.moa.backend.model.Role


data class UserSlimDto (
    var id: Long = 0,

    var firstName: String,

    var lastName: String,

    var email: String,

    var role: Role,

    )