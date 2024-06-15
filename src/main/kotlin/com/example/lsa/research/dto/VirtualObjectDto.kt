package com.example.lsa.research.dto

data class VirtualObjectDto(
    val virtualObjectId: Long,
    val name: String,
    val text: String?,
    val imageName: String?,
    val soundName: String?,
    val manualId: Long,
    val position: List<Int>,
    val size: List<Int>
)