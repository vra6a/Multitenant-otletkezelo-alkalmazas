package com.moa.backend.multitenancy

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

@Component
class TenantConnectionProvider(
    private val dataSource: DataSource
) : MultiTenantConnectionProvider {
    override fun isUnwrappableAs(p0: Class<*>?): Boolean {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> unwrap(p0: Class<T>?): T {
        TODO("Not yet implemented")
    }

    override fun getAnyConnection(): Connection {
        return dataSource.connection
    }

    override fun releaseAnyConnection(connection: Connection) {
        connection.close()
    }

    override fun getConnection(tenantIdentifier: String?): Connection {
        val connection = dataSource.connection
        try {
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS `$tenantIdentifier`;")
            connection.createStatement().execute("USE `$tenantIdentifier`;")
        } catch (e: SQLException) {
            println("Error switching to schema: ${e.message}")
        }
        return connection
    }

    override fun releaseConnection(tenantIdentifier: String?, connection: Connection) {
        connection.close()
    }

    override fun supportsAggressiveRelease(): Boolean = false
}