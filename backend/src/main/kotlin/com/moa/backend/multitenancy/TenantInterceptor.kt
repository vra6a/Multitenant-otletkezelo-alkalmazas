package com.moa.backend.multitenancy

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class TenantInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val tenantId = request.getHeader("X-Tenant-Id")
        println("Incoming Headers: ${request.headerNames.toList().joinToString(", ") { "${it}: ${request.getHeader(it)}" }}")

        if (tenantId != null) {
            TenantContext.setCurrentTenant(tenantId)
            println("TenantId: $tenantId from TenantInterceptor")
        } else {
            println("No TenantId found in request headers.")
        }
        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        TenantContext.clear()
    }
}