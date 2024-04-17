package com.example.lsa.member.dto

data class UserDto(
    val username: String,
    val password: String,
    val role: String,
    val labNames: Set<String>,
    val staffId: String,   // 사용자의 학번(사번)
    val name: String       // 사용자의 이름
)