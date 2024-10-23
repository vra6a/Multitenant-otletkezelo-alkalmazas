package com.moa.backend.security.config

import com.moa.backend.multitenancy.TenantContext
import com.moa.backend.utility.ErrorException
import com.moa.backend.utility.WebResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Required
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var jwtService: JwtService

    @Autowired
    lateinit var userDetailsService: UserDetailsService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        val jwt: String?
        val userEmail: String?

        val tenant: String? = request.getHeader("X-Tenant-Id")
        if (tenant != null) {
            TenantContext.setCurrentTenant(tenant)
        }

        val currentTenant = TenantContext.getCurrentTenant()

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        jwt = authHeader.substring(7)
        userEmail = jwtService.extractUsername(jwt)

        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            if (currentTenant == null) {
                throw ErrorException("Tenant identifier is missing!")
            }

            logger.info("Attempting to load user by email: $userEmail for tenant: $currentTenant")
            val userDetails: UserDetails = userDetailsService.loadUserByUsername(userEmail)

            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            } else {
                returnUnauthorizedResponse(response, "JWT Token is invalid!")
                return
            }
        } else {
            returnUnauthorizedResponse(response, "JWT Token is invalid!")
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun returnUnauthorizedResponse(response: HttpServletResponse, message: String) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        val webResponse = WebResponse<String>(
            code = HttpStatus.UNAUTHORIZED.value(),
            message = message,
            data = null
        )

        // Convert WebResponse to JSON and write it to the response
        val objectMapper = ObjectMapper()
        response.writer.write(objectMapper.writeValueAsString(webResponse))
    }
}