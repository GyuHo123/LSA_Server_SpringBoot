package com.example.lsa.portal.repo

import com.example.lsa.portal.entity.SharedManual
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PortalRepository: JpaRepository<SharedManual, Long> {

}