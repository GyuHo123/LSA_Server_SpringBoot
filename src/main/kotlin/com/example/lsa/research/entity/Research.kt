package com.example.lsa.research.entity

import jakarta.persistence.*

@Entity
@Table(name = "research_list")
data class Research(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "lab_id", nullable = false)
    val labId: Long
)