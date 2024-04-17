package com.example.lsa.common.auth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtTokenFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {
    private val pathMatcher = AntPathMatcher()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if(isSkippedPath(request)){
            filterChain.doFilter(request, response)
        }
        else{
            val token = getTokenFromRequest(request)
            token?.let {
                val username = jwtTokenUtil.getUsernameFromToken(it)
                if (jwtTokenUtil.validateToken(it, userDetailsService.loadUserByUsername(username))) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    val authentication = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.substring(7)
    }

    private fun isSkippedPath(request: HttpServletRequest): Boolean {
        val pathsToSkip = listOf("/api/users/register", "/api/users/login")
        return pathsToSkip.any { path -> pathMatcher.match(path, request.servletPath) }
    }
}
