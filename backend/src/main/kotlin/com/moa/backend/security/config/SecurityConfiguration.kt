package com.moa.backend.security.config

import com.moa.backend.multitenancy.TenantFilter
import com.moa.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.bind.annotation.CrossOrigin

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = ["http://localhost:4200"])
class SecurityConfiguration {

    @Autowired
    lateinit var jwtAuthFilter: JwtAuthenticationFilter

    @Autowired
    lateinit var tenantFilter: TenantFilter

    @Autowired
    lateinit var authenticationProvider: AuthenticationProvider

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors()
            .and()
            .csrf().disable()
            .authorizeHttpRequests()
                .antMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .antMatchers("/api/scoreSheet/{id}").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api/scoreSheet/create/{id}").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api/scoreSheet/{id}/save").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api//idea/ideasToScore").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api/idea/{id}/scoreSheets").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api/score/getIdeas").hasAnyAuthority("JURY","ADMIN")
                .antMatchers("/api/score/getScoredIdeaBoxes").hasAnyAuthority("ADMIN")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(tenantFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }
}