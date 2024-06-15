package com.example.lsa.research.repo

import com.example.lsa.research.entity.Research
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResearchRepository : JpaRepository<Research, Long> {
    fun findByLabId(labId: Long): List<Research>
}
