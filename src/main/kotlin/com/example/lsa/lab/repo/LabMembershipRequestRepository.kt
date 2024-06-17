package com.example.lsa.lab.repo

import com.example.lsa.lab.entity.LabMembershipRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LabMembershipRequestRepository : JpaRepository<LabMembershipRequest, Long> {
    fun findAllByLab_Id(labId: Long): List<LabMembershipRequest>
    fun findAllByUser_Id(userId: Long): List<LabMembershipRequest>
    fun findByUserIdAndLabId(userId: Long, labId: Long): LabMembershipRequest?
}
