package com.example.lsa.common.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenUtil {

    @Value("\${jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${jwt.expiration}")
    private val expirationTime: Long = 3600  // 토큰 유효 시간 (예: 3600초)

    fun generateToken(userDetails: UserDetails): String {
        val claims = Jwts.claims().setSubject(userDetails.username)
        claims["authorities"] = userDetails.authorities.joinToString(",") { it.authority }

        val nowMillis = System.currentTimeMillis()
        val expirationMillis = nowMillis + expirationTime * 1000

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date(nowMillis))
            .setExpiration(Date(expirationMillis))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    fun getUsernameFromToken(token: String): String {
        return getAllClaimsFromToken(token).subject
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getAllClaimsFromToken(token).expiration
        return expiration.before(Date())
    }
}
