package com.example.lsa.member.entity

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "lab_membership_requests")
class LabMembershipRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column
    val user: Long,

    @Column(name = "lab_id", nullable = false)
    val lab: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: MembershipStatus = MembershipStatus.PENDING
)

enum class MembershipStatus {
    PENDING, ACCEPTED, REJECTED
}
