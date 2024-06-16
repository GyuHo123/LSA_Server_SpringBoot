package com.example.lsa.common.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenUtil: JwtTokenUtil,
    @Lazy private val userDetailsService: UserDetailsService
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/users/register", "/api/users/login", "/api/users/verify").permitAll()
                    .requestMatchers("api/labs/respond-to-request").hasRole("RESEARCHER")
                    .requestMatchers("api/labs/request-membership").hasRole("STUDENT")
                    .anyRequest().authenticated()
            }
            .csrf { csrf -> csrf.disable() }
            .cors { cors ->
                cors.configurationSource {
                    val configuration = CorsConfiguration().apply {
                        addAllowedOrigin("https://study.kgyuho.dev")
                        allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        allowedHeaders = listOf("*")
                        allowCredentials = true
                    }
                    configuration
                }
            }

            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtTokenFilter(): JwtTokenFilter {
        return JwtTokenFilter(jwtTokenUtil, userDetailsService)
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val user = User.withUsername("user")
            .password(passwordEncoder().encode("password"))
            .authorities(SimpleGrantedAuthority("ROLE_USER"))
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
