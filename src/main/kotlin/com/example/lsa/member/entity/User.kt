package com.example.lsa.member.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val username: String,

    @NotBlank
    var password: String,

    @Enumerated(EnumType.STRING)
    val role: Role,

    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_labs",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "lab_id")]
    )
    val labs: Set<Lab> = HashSet(),

    @Column(nullable = false)  // 학번(사번) 필드 추가
    val staffId: String,

    @Column(nullable = false)  // 이름 필드 추가
    val name: String
)
