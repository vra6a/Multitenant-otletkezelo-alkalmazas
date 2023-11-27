package com.moa.backend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BackendApplicationTests {

	@Test
	fun alwaysTrue() {
		System.out.println("JDBC URL: " + jdbcUrl);
		System.out.println("Username: " + dbUsername);
		System.out.println("Password: " + dbPassword);
	}

}
