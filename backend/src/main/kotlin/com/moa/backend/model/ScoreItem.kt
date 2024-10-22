package com.moa.backend.model

import org.hibernate.annotations.Filter
import org.hibernate.annotations.FilterDef
import org.hibernate.annotations.Filters
import org.hibernate.annotations.ParamDef
import javax.persistence.*

@Entity
@FilterDef(name = "tenantFilter", parameters = [ParamDef(name = "tenantId", type = "string")])
@Filters(
    Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
)
data class ScoreItem (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "tenant_id", nullable = false)
    var tenantId: String,

    @Enumerated(EnumType.STRING)
    var type: ScoreType,

    @ManyToOne
    @JoinColumn(name = "scoreSheet_id")
    var scoreSheet: ScoreSheet,

    var title: String,

    var score: Int?,

    var text: String?,
)
