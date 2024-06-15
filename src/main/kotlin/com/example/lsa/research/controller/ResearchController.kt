package com.example.lsa.research.controller

import com.example.lsa.common.dto.FileUploadResponse
import com.example.lsa.common.service.FileService
import com.example.lsa.research.dto.*
import com.example.lsa.research.service.*
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files

@RestController
@RequestMapping("/api/research")
class ResearchController(
    private val researchService: ResearchService,
    private val manualService: ManualService,
    private val virtualObjectService: VirtualObjectService,
    private val fileService: FileService,
    private val educationService: EducationService
) {

    @GetMapping("/{labId}/find-research")
    fun findResearchByLabId(@PathVariable labId: Long): ResponseEntity<List<ResearchDto>> {
        val researchList = researchService.findResearchByLabId(labId)
        return if (researchList.isNotEmpty()) {
            ResponseEntity.ok(researchList)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @PostMapping("/{labId}/create-research")
    fun createResearch(@PathVariable labId: Long, @RequestBody researchCreateDto: ResearchCreateDto): ResponseEntity<Map<String, Long>> {
        val research = researchService.createResearch(labId, researchCreateDto.researchName)
        return ResponseEntity.ok(mapOf("researchId" to research.researchId))
    }

    @PostMapping("/{labId}/edit-research")
    fun editResearch(@PathVariable labId: Long, @RequestParam researchId: Long, @RequestBody researchCreateDto: ResearchCreateDto): ResponseEntity<String> {
        researchService.editResearch(researchId, researchCreateDto.researchName)
        return ResponseEntity.ok("연구 이름 수정 완료")
    }

    @PostMapping("/{labId}/delete-research")
    fun deleteResearch(@PathVariable labId: Long, @RequestParam researchId: Long): ResponseEntity<String> {
        researchService.deleteResearch(researchId)
        return ResponseEntity.ok("연구 삭제 완료")
    }

    @GetMapping("/manual/find-manual")
    fun findManualByResearchId(@RequestParam researchId: Long): ResponseEntity<List<ManualDto>> {
        val manualList = manualService.findManualByResearchId(researchId)
        return if (manualList.isNotEmpty()) {
            ResponseEntity.ok(manualList)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @PostMapping("/manual/{labId}/create-manual")
    fun createManual(@PathVariable labId: Long, @RequestParam researchId: Long, @RequestBody manualCreateDto: ManualCreateDto): ResponseEntity<Map<String, Long>> {
        val manual = manualService.createManual(researchId, manualCreateDto.name)
        return ResponseEntity.ok(mapOf("manualId" to manual.manualId))
    }

    @PostMapping("/manual/edit-manual")
    fun editManual(@RequestParam manualId: Long, @RequestBody manualCreateDto: ManualCreateDto): ResponseEntity<String> {
        manualService.editManual(manualId, manualCreateDto.name)
        return ResponseEntity.ok("매뉴얼 수정 완료")
    }

    @PostMapping("/manual/delete-manual")
    fun deleteManual(@RequestParam manualId: Long): ResponseEntity<String> {
        manualService.deleteManual(manualId)
        return ResponseEntity.ok("매뉴얼 삭제 완료")
    }

    @GetMapping("/vobject/find-virtual-object-list")
    fun findVirtualObjectListByManualId(@RequestParam manualId: Long): ResponseEntity<List<Int>> {
        val virtualObjectList = virtualObjectService.findVirtualObjectListByManualId(manualId)
        return ResponseEntity.ok(virtualObjectList)
    }

    @GetMapping("/vobject/find-virtual-object-info")
    fun findVirtualObjectInfoById(@RequestParam virtualObjectId: Long): ResponseEntity<VirtualObjectDto> {
        val virtualObject = virtualObjectService.findVirtualObjectInfoById(virtualObjectId)
        return ResponseEntity.ok(virtualObject)
    }

    @PostMapping("/vobject/create-virtual-object")
    fun createVirtualObject(@RequestBody virtualObjectCreateDto: VirtualObjectCreateDto): ResponseEntity<Map<String, Long>> {
        val virtualObject = virtualObjectService.createVirtualObject(virtualObjectCreateDto)
        return ResponseEntity.ok(mapOf("virtualObjectId" to virtualObject.virtualObjectId))
    }

    @PostMapping("/vobject/edit-virtual-object")
    fun editVirtualObject(@RequestParam virtualObjectId: Long, @RequestBody virtualObjectDto: VirtualObjectDto): ResponseEntity<String> {
        virtualObjectService.editVirtualObject(virtualObjectId, virtualObjectDto)
        return ResponseEntity.ok("가상 객체 수정 완료")
    }

    @PostMapping("/vobject/delete-virtual-object")
    fun deleteVirtualObject(@RequestParam virtualObjectId: Long): ResponseEntity<String> {
        virtualObjectService.deleteVirtualObject(virtualObjectId)
        return ResponseEntity.ok("가상 객체 삭제 완료")
    }

    @PostMapping("/vobject/upload-file")
    fun uploadFile(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("manualId") manualId: Long,
        @RequestParam("virtualObjectId") virtualObjectId: Long
    ): FileUploadResponse {
        return fileService.uploadFile(file, manualId, virtualObjectId)
    }

    @GetMapping("/vobject/files/{fileName:.+}")
    fun previewFile(@PathVariable fileName: String): ResponseEntity<Resource> {
        val resource = fileService.loadFileAsResource(fileName)
        return ResponseEntity.ok()
            .contentType(Files.probeContentType(resource.file.toPath()).let { MediaType.parseMediaType(it) })
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${resource.filename}\"")
            .body(resource)
    }

    @PostMapping("/education/{manualId}/complete-education")
    fun completeEducation(@PathVariable manualId: Long, @RequestParam userId: Long): ResponseEntity<String> {
        educationService.completeEducation(userId, manualId)
        return ResponseEntity.ok("교육을 완료했습니다")
    }

    @GetMapping("/education/{manualId}/status")
    fun getEducationStatus(@PathVariable manualId: Long, @RequestParam userId: Long): ResponseEntity<String> {
        val educationDto = educationService.getEducationStatus(userId, manualId)
        return if (educationDto != null) {
            if(educationDto.complete) ResponseEntity.ok("교육 완료")
            else ResponseEntity.ok("교육 완료 X")
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/education/{manualId}/start")
    fun startEducation(@PathVariable manualId: Long, @RequestBody educationCreateDto: EducationCreateDto): ResponseEntity<EducationDto> {
        val educationDto = educationService.startEducation(educationCreateDto)
        return ResponseEntity.ok(educationDto)
    }
}
