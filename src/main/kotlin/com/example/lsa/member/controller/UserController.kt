package com.example.lsa.member.controller

import com.example.lsa.member.dto.*
import com.example.lsa.member.service.UserService
import com.example.lsa.common.auth.JwtTokenUtil
import com.example.lsa.member.service.CustomUserDetails
import com.example.lsa.member.service.CustomUserDetailsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
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
            userService.registerUser(userDto)
            ResponseEntity.ok("인증메일이 전송되었으니 인증을 완료해주세요")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/verify")
    fun verifyCode(@RequestBody verifyDto: VerifyDto): ResponseEntity<Any> {
        return try {
            userService.completeRegistration(verifyDto.userDto, verifyDto.code)
            ResponseEntity.ok("회원가입 완료")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody loginDto: LoginDto): ResponseEntity<Any> {
        return try {
            val user = userDetailsService.loadUserByUsername(loginDto.username) as CustomUserDetails
            if (passwordEncoder.matches(loginDto.password, user.password)) {
                val token = jwtTokenUtil.generateToken(user)
                ResponseEntity.ok(mapOf("userId" to user.id, "token" to token))
            } else {
                ResponseEntity.badRequest().body("올바르지 않은 비밀번호")
            }
        } catch (e: UsernameNotFoundException) {
            ResponseEntity.badRequest().body("올바르지 않은 이메일입니다")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 중 에러 발생")
        }
    }

    @GetMapping("/{userId}/find-user-info")
    fun getUserInfo(@PathVariable userId: Long): ResponseEntity<UserInfoDto> {
        val user = userService.getUserInfo(userId)
        return ResponseEntity.ok(user)
    }

}
