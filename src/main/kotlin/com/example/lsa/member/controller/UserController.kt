package com.example.lsa.member.controller

import com.example.lsa.member.dto.*
import com.example.lsa.member.service.UserService
import com.example.lsa.common.auth.JwtTokenUtil
import com.example.lsa.member.service.CustomUserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenUtil: JwtTokenUtil,
    private val userDetailsService: CustomUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody userDto: UserDto): ResponseEntity<Any> {
        return try {
            val user = userService.registerUser(userDto)
            ResponseEntity.ok(user)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDto: LoginDto): ResponseEntity<Any> {
        return try {
            val user = userDetailsService.loadUserByUsername(loginDto.username)
            if (user != null && passwordEncoder.matches(loginDto.password, user.password)) {
                val token = jwtTokenUtil.generateToken(user)
                ResponseEntity.ok(mapOf("token" to token))
            } else {
                ResponseEntity.badRequest().body("PASSWORD is not correct")
            }
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.badRequest().body("Invalid username")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during the login process.")
        }
    }
}
