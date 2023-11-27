package com.moa.backend

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendApplicationTests {

	@Value("\${spring.datasource.url}")
	private val jdbcUrl: String? = null

	@Value("\${spring.datasource.username}")
	private val dbUsername: String? = null

	@Value("\${spring.datasource.password}")
	private val dbPassword: String? = null

	@Test
	fun alwaysTrue() {
		System.out.println("JDBC URL: " + jdbcUrl);
		System.out.println("Username: " + dbUsername);
		System.out.println("Password: " + dbPassword);
	}

}
