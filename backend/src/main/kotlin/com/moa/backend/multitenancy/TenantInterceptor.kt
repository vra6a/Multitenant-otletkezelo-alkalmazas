package com.moa.backend.multitenancy

import org.hibernate.Session
import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

@Component
class TenantInterceptor(
    @Autowired private val entityManagerFactory: EntityManagerFactory
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tenantId = TenantContext.getCurrentTenant()
        println("TenantId: $tenantId from TenantInterceptor")
        if (tenantId != null) {
            val entityManager = entityManagerFactory.createEntityManager()
            val session = entityManager.unwrap(Session::class.java)
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId)
            // Store the entityManager in a thread-local or similar context for later use
            TenantContext.setEntityManager(entityManager)
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        // Clean up after the request
        TenantContext.clear()
    }
}