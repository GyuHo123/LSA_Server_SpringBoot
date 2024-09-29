package com.example.lsa.portal.service

import com.example.lsa.portal.dto.SharedManualDTO
import com.example.lsa.portal.entity.SharedManual
import com.example.lsa.portal.entity.toDTO
import com.example.lsa.portal.repo.PortalRepository
import org.springframework.stereotype.Service

@Service
class ShareManualService(
    private val portalRepository: PortalRepository
){
    fun getAllSharedManual(): MutableList<SharedManualDTO>{
        val allSharedManualList: List<SharedManual> = portalRepository.findAll()
        val allSharedManualDTOList: MutableList<SharedManualDTO> = mutableListOf()
        for (sharedManual in allSharedManualList) {
            allSharedManualDTOList.add(sharedManual.toDTO())
        }
        return allSharedManualDTOList
    }
}