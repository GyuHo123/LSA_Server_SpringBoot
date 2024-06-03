package com.example.lsa.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_labs")
data class UserLab(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long,

    @Column(name = "lab_id", nullable = false)
    var labId: Long
)
