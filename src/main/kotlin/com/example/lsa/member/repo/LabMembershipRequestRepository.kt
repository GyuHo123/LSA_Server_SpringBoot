package com.example.lsa.member.repo

import com.example.lsa.member.entity.LabMembershipRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LabMembershipRequestRepository : JpaRepository<LabMembershipRequest, Long> {

}