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
data class Tag (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "tenant_id", nullable = false)
    var tenantId: String,

    var name: String,

    @ManyToMany(mappedBy = "tags")
    var taggedIdeas: MutableList<Idea>?


)