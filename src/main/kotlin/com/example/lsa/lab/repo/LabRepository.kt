package com.example.lsa.lab.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.example.lsa.lab.entity.Lab


@Repository
interface LabRepository : JpaRepository<Lab, Long> {
    fun findAllByDirectorStaffId(directorStaffId: String): List<Lab>
}