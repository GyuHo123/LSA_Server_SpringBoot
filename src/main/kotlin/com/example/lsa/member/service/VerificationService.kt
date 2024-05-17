package com.example.lsa.member.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import kotlin.random.Random

@Service
class VerificationService(private val redisTemplate: RedisTemplate<String, String>) {

    private val valueOps: ValueOperations<String, String> = redisTemplate.opsForValue()

    fun generateAndSendVerificationCode(email: String): String {
        val code = Random.nextInt(100000, 999999).toString()
        valueOps.set(email, code, 10, TimeUnit.MINUTES)
        return code
    }

    fun verifyCode(email: String, code: String): Boolean {
        val storedCode = valueOps.get(email)
        return if (storedCode != null && storedCode == code) {
            redisTemplate.delete(email)
            true
        } else {
            false
        }
    }
}
