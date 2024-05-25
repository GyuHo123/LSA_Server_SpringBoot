package com.example.lsa.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_labs")
data class UserLab(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)  // Ensure this matches database column name
    var userId: Long,

    @Column(name = "lab_id", nullable = false)  // Ensure this matches database column name
    var labId: Long
)

