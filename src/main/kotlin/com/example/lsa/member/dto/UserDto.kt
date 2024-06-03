package com.example.lsa.member.dto

data class UserDto(
    val username: String,
    val password: String,
    val role: String,
    val labs: List<Long> = listOf(),
    val labNames: List<String> = listOf(),
    val staffId: String,
    val name: String,
    val dept: String
)