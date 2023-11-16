import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.9"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "com.moa"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    testImplementation("junit:junit:4.13.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.10.4")
	testImplementation("org.springframework.security:spring-security-test")
	implementation("org.mapstruct:mapstruct:1.5.3.Final")
	annotationProcessor ("org.mapstruct:mapstruct-processor:1.5.3.Final")
	implementation ("io.github.microutils:kotlin-logging-jvm:2.0.11")
	implementation("ch.qos.logback:logback-classic:1.2.6")
	runtimeOnly("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
