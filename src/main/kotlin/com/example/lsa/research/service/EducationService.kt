package com.example.lsa.research.service

import com.example.lsa.research.dto.EducationCreateDto
import com.example.lsa.research.dto.EducationDto
import com.example.lsa.research.entity.Education
import com.example.lsa.research.repo.EducationRepository
import org.springframework.stereotype.Service

@Service
class EducationService(
    private val educationRepository: EducationRepository
) {

    fun completeEducation(userId: Long, manualId: Long): EducationDto {
        val existingEducation = educationRepository.findByUserIdAndManualId(userId, manualId)
        val education = if (existingEducation != null) {
            existingEducation.complete = true
            existingEducation
        } else {
            Education(userId = userId, manualId = manualId, complete = true)
        }
        educationRepository.save(education)
        return education.toDto()
    }


    fun getEducationStatus(userId: Long, manualId: Long): EducationDto? {
        val education = educationRepository.findByUserIdAndManualId(userId, manualId)
        return education?.toDto()
    }

    fun startEducation(educationCreateDto: EducationCreateDto): EducationDto {
        val education = Education(
            userId = educationCreateDto.userId,
            manualId = educationCreateDto.manualId,
            complete = false
        )
        educationRepository.save(education)
        return education.toDto()
    }

    private fun Education.toDto(): EducationDto =
        EducationDto(educationId = this.id, userId = this.userId, manualId = this.manualId, complete = this.complete)
}
