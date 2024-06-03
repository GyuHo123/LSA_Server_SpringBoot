package com.example.lsa.member.repo

import org.springframework.data.jpa.repository.JpaRepository
import com.example.lsa.member.entity.UserLab

interface UserLabRepository : JpaRepository<UserLab, Long> {
    fun findAllByLabId(labId: Long): List<UserLab>
    fun findAllByUserId(userId: Long): List<UserLab>
    fun findByUserIdAndLabId(userId: Long, labId: Long): UserLab?
}
