package com.example.lsa.member.repo

import org.springframework.data.jpa.repository.JpaRepository
import com.example.lsa.member.entity.UserLab

interface UserLabRepository : JpaRepository<UserLab, Long> {
}