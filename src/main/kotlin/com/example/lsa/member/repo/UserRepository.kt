package com.example.lsa.member.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.example.lsa.member.entity.User

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun findByStaffId(staffId: String): User?  // Add this method to find by staffId
}
