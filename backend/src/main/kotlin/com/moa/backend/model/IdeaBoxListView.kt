package com.moa.backend.model

import java.util.*
import javax.persistence.*

data class IdeaBoxListView (

    var id: Long = 0,
    var name: String,
    var endDate: Date,
)