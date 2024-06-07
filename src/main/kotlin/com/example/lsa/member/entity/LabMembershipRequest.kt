package com.example.lsa.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "lab_membership_requests")
class LabMembershipRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    val lab: Lab,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: MembershipStatus = MembershipStatus.PENDING
)

enum class MembershipStatus {
    PENDING, ACCEPTED, REJECTED
}
