package com.moa.backend.security.config

import com.moa.backend.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.SecretKey
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class JwtService {

    private final var SECRET_KEY: String = "2948404D635166546A576E5A7234753777217A25432A462D4A614E645267556B"

    fun extractUsername(jwt: String): String {
        return extractClaim(jwt, Claims::getSubject)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return  Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 24))
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        var username: String = extractUsername(token)
        return username.equals(userDetails.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
       return  extractClaim(token, Claims::getExpiration)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims: Claims = extractClaims(token)
        return claimsResolver(claims)
    }

    private fun extractClaims(jwt: String): Claims {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(jwt)
            .getBody()
    }

    private fun getSignInKey(): Key {
        val keyBites = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBites)
    }

}
