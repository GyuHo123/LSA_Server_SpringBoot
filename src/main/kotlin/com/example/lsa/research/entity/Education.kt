package com.example.lsa.research.entity

import jakarta.persistence.*

@Entity
@Table(name = "education")
data class Education(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "manual_id", nullable = false)
    val manualId: Long,

    @Column(name = "complete", nullable = false)
    var complete: Boolean
)
