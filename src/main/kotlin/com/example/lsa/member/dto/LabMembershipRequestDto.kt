package com.example.lsa.member.dto

data class LabMembershipRequestDto(
    val userName: String,
    val staffId: String,
    val labId: Long,
    val labName: String,
    val role: String,
    val dept: String,
    val labdept: String,
    val requestId: Long
)