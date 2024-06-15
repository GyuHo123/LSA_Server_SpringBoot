package com.example.lsa.research.repo

import com.example.lsa.research.entity.VirtualObject
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface VirtualObjectRepository : JpaRepository<VirtualObject, Long> {
    fun findByManualId(manualId: Long): List<VirtualObject>
}
