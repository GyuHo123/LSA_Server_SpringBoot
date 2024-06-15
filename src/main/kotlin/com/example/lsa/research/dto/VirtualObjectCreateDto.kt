package com.example.lsa.research.dto

data class VirtualObjectCreateDto(
    val position: List<Int>,
    val size: List<Int>,
    val name: String,
    val text: String?,
    val imageName: String?,
    val soundName: String?,
    val manualId: Long
)