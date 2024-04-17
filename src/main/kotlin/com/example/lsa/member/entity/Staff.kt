package com.example.lsa.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "staff")
class Staff(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val staffId: String,

    @Column(nullable = false)
    val name: String
)
