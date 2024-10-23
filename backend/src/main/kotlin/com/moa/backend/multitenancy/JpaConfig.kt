package com.moa.backend.multitenancy
import com.moa.backend.multitenancy.CurrentTenantIdentifierResolver
import com.moa.backend.multitenancy.TenantConnectionProvider
import org.hibernate.cfg.AvailableSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class JpaConfig(
    @Autowired private val tenantConnectionProvider: TenantConnectionProvider,
    @Autowired private val tenantIdentifierResolver: CurrentTenantIdentifierResolver
) {

    @Bean
    fun entityManagerFactory(dataSource: DataSource, jpaProperties: JpaProperties, environment: Environment): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        val factory = LocalContainerEntityManagerFactoryBean()

        factory.dataSource = dataSource
        factory.setPackagesToScan("com.moa.backend")
        factory.jpaVendorAdapter = vendorAdapter

        val jpaPropertiesMap = HashMap<String, Any>().apply {
            putAll(jpaProperties.properties)
            put(AvailableSettings.MULTI_TENANT, "SCHEMA")
            put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, tenantConnectionProvider)
            put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver)
            put(AvailableSettings.HBM2DDL_AUTO, "update")

        }

        factory.setJpaPropertyMap(jpaPropertiesMap)
        return factory
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): JpaTransactionManager {
        return JpaTransactionManager(entityManagerFactory)
    }
}