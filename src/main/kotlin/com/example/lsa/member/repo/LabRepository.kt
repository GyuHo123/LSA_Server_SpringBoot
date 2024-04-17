package com.example.lsa.member.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.example.lsa.member.entity.Lab


@Repository
interface LabRepository : JpaRepository<Lab, Long> {
    fun findAllByDirectorStaffId(directorStaffId: String): List<Lab>
}