package com.example.lsa.research.repo

import com.example.lsa.research.entity.Education
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EducationRepository : JpaRepository<Education, Long> {
    fun findByUserIdAndManualId(userId: Long, manualId: Long): Education?
}
