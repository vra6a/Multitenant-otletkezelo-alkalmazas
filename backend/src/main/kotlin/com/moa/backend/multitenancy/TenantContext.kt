package com.moa.backend.multitenancy

import javax.persistence.EntityManager

object TenantContext {
    private val currentTenant = ThreadLocal<String>()
    private val entityManagerThreadLocal = ThreadLocal<EntityManager>()

    fun setCurrentTenant(tenantId: String) {
        currentTenant.set(tenantId)
    }

    fun getCurrentTenant(): String? = currentTenant.get()

    fun clear() {
        currentTenant.remove()
        entityManagerThreadLocal.get()?.close()
        entityManagerThreadLocal.remove()
    }

    fun setEntityManager(entityManager: EntityManager) {
        entityManagerThreadLocal.set(entityManager)
    }

    fun getEntityManager(): EntityManager? = entityManagerThreadLocal.get()
}