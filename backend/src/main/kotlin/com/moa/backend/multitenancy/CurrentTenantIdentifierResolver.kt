package com.moa.backend.multitenancy

import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.stereotype.Component

@Component
class CurrentTenantIdentifierResolver : CurrentTenantIdentifierResolver {

    override fun resolveCurrentTenantIdentifier(): String {
        return TenantContext.getCurrentTenant() ?: "default_tenant"
    }

    override fun validateExistingCurrentSessions(): Boolean {
        return true
    }
}