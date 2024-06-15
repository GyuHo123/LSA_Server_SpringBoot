package com.example.lsa.research.service

import com.example.lsa.research.dto.ManualDto
import com.example.lsa.research.entity.Manual
import com.example.lsa.research.repo.ManualRepository
import org.springframework.stereotype.Service

@Service
class ManualService(
    private val manualRepository: ManualRepository
) {

    fun findManualByResearchId(researchId: Long): List<ManualDto> =
        manualRepository.findByResearchId(researchId).map { it.toDTO() }

    fun createManual(researchId: Long, name: String): ManualDto {
        val manual = Manual(name = name, researchId = researchId)
        return manualRepository.save(manual).toDTO()
    }

    fun editManual(manualId: Long, name: String): String {
        val manual = manualRepository.findById(manualId).orElseThrow { RuntimeException("매뉴얼 없음") }
        manualRepository.save(manual.copy(name = name))
        return "성공"
    }

    fun deleteManual(manualId: Long): String {
        manualRepository.deleteById(manualId)
        return "성공"
    }

    private fun Manual.toDTO(): ManualDto =
        ManualDto(manualId = this.id, manualName = this.name, researchId = this.researchId)
}