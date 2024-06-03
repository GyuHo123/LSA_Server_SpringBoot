package com.example.lsa.member.dto

data class VerifyDto(
    val email: String,
    val code: String,
    val userDto: UserDto
)