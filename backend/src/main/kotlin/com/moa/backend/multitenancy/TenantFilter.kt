package com.moa.backend.multitenancy

import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component
class TenantFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val tenantId = httpServletRequest.getHeader("X-Tenant-Id")

        if (tenantId != null) {

            TenantContext.setCurrentTenant(tenantId)
            println(TenantContext.getCurrentTenant() + "from TenantFilter")
        }

        try {
            chain.doFilter(request, response)
        } finally {
            TenantContext.clear()
        }
    }

    override fun init(filterConfig: FilterConfig?) {}
    override fun destroy() {}
}