package com.example.lsa.research.service

import com.example.lsa.research.dto.ResearchDto
import com.example.lsa.research.entity.Research
import com.example.lsa.research.repo.ResearchRepository
import org.springframework.stereotype.Service

@Service
class ResearchService(
    private val researchRepository: ResearchRepository
) {

    fun findResearchByLabId(labId: Long): List<ResearchDto> =
        researchRepository.findByLabId(labId).map { it.toDTO() }

    fun createResearch(labId: Long, name: String): ResearchDto {
        val research = Research(name = name, labId = labId)
        return researchRepository.save(research).toDTO()
    }

    fun editResearch(researchId: Long, name: String): String {
        val research = researchRepository.findById(researchId).orElseThrow { RuntimeException("연구를 찾을 수 없음") }
        researchRepository.save(research.copy(name = name))
        return "성공"
    }

    fun deleteResearch(researchId: Long): String {
        researchRepository.deleteById(researchId)
        return "성공"
    }

    private fun Research.toDTO(): ResearchDto =
        ResearchDto(researchId = this.id, researchName = this.name, labId = this.labId)
}