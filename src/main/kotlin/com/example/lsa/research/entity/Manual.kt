package com.example.lsa.research.entity

import jakarta.persistence.*

@Entity
@Table(name = "manual")
data class Manual(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "research_id", nullable = false)
    val researchId: Long
)
