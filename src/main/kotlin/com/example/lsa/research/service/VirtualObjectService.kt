package com.example.lsa.research.service

import com.example.lsa.research.dto.VirtualObjectCreateDto
import com.example.lsa.research.dto.VirtualObjectDto
import com.example.lsa.research.entity.VirtualObject
import com.example.lsa.research.repo.VirtualObjectRepository
import org.springframework.stereotype.Service

@Service
class VirtualObjectService(
    private val virtualObjectRepository: VirtualObjectRepository
) {

    fun findVirtualObjectListByManualId(manualId: Long): List<Int> =
        virtualObjectRepository.findByManualId(manualId).map { it.id.toInt() }

    fun findVirtualObjectInfoById(virtualObjectId: Long): VirtualObjectDto =
        virtualObjectRepository.findById(virtualObjectId).orElseThrow { RuntimeException("가상객체 없음") }
            .toDTO()

    fun createVirtualObject(virtualObjectCreateDTO: VirtualObjectCreateDto): VirtualObjectDto {
        val virtualObject = VirtualObject(
            name = virtualObjectCreateDTO.name,
            text = virtualObjectCreateDTO.text,
            imageName = virtualObjectCreateDTO.imageName,
            soundName = virtualObjectCreateDTO.soundName,
            position = virtualObjectCreateDTO.position,
            manualId = virtualObjectCreateDTO.manualId,
            size = virtualObjectCreateDTO.size
        )
        return virtualObjectRepository.save(virtualObject).toDTO()
    }

    fun editVirtualObject(virtualObjectId: Long, virtualObjectDTO: VirtualObjectDto): String {
        val virtualObject = virtualObjectRepository.findById(virtualObjectId).orElseThrow { RuntimeException("가상 객체 없음") }
        virtualObjectRepository.save(virtualObject.copy(
            name = virtualObjectDTO.name,
            text = virtualObjectDTO.text,
            imageName = virtualObjectDTO.imageName,
            soundName = virtualObjectDTO.soundName,
            manualId = virtualObjectDTO.manualId,
            position = virtualObjectDTO.position,
            size = virtualObjectDTO.size
        ))
        return "성공"
    }

    fun deleteVirtualObject(virtualObjectId: Long): String {
        virtualObjectRepository.deleteById(virtualObjectId)
        return "성공"
    }

    private fun VirtualObject.toDTO(): VirtualObjectDto =
        VirtualObjectDto(
            virtualObjectId = this.id,
            name = this.name,
            text = this.text,
            imageName = this.imageName,
            soundName = this.soundName,
            position = this.position,
            manualId = this.manualId,
            size = this.size
        )
}