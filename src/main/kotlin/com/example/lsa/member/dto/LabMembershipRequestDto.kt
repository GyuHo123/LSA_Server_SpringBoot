package com.example.lsa.member.dto

data class LabMembershipRequestDto(
    val userName: String,
    val userStaffId: String,
    val labId: Long,
    val labName: String
)
