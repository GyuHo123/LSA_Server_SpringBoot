package com.example.lsa.portal.controller

import com.example.lsa.portal.service.ShareManualService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/portal/sharemanual")
class PortalController(
    private val sharedManualService: ShareManualService
) {
    @RequestMapping("/all")
    fun findAllSharedManual(): ResponseEntity<Any> {
        val sharedManualList = sharedManualService.getAllSharedManual()
        return if (sharedManualList.isNotEmpty()) {
            ResponseEntity.ok(sharedManualList)
        } else {
            ResponseEntity.badRequest().body("조회된 공유 매뉴얼이 없습니다")
        }
    }
}