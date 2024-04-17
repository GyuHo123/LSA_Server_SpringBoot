package com.example.lsa.member.repo

import com.example.lsa.member.entity.Staff
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StaffRepository : JpaRepository<Staff, Long> {
    fun findByStaffIdAndName(staffId: String, name: String): Staff?
}
