package com.example.lsa.lab.dto

data class LabMembershipRequestDto(
    val userId: Long?,
    val userName: String,
    val staffId: String,
    val labId: Long,
    val labName: String,
    val role: String,
    val dept: String,
    val labDept: String,
    val requestId: Long
)