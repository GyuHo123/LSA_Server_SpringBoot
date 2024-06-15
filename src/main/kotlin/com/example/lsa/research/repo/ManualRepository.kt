package com.example.lsa.research.repo

import com.example.lsa.research.entity.Manual
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ManualRepository : JpaRepository<Manual, Long> {
    fun findByResearchId(researchId: Long): List<Manual>
}
