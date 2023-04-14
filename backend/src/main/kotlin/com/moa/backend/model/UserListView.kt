package com.moa.backend.model

import javax.persistence.EnumType
import javax.persistence.Enumerated


data class UserListView (
    var id: Long,
    var firstName: String,

    var lastName: String,

    var email: String,

    @Enumerated(EnumType.STRING)
    var role: Role,
)