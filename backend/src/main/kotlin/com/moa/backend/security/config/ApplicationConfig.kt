package com.moa.backend.security.config

import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfig {

    @Autowired
    lateinit var userRepository: UserRepository

    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { email ->
            val userOptional = userRepository.findByEmail(email)

            val user = userOptional.orElseThrow {
                UsernameNotFoundException("User not found with email: $email")
            }

            return@UserDetailsService user
        }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val authProvider: DaoAuthenticationProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService())
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}