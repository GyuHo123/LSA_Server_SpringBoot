package com.example.lsa.lab.entity

import jakarta.persistence.*

@Entity
class Lab(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true, nullable = false)
    var name: String,

    var directorStaffId: String,

    @Column(nullable = false)
    var dept: String
)
