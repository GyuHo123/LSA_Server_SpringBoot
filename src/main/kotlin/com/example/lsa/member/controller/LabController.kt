package com.example.lsa.member.controller

import com.example.lsa.member.service.LabService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/labs")
class LabController(
    private val labService: LabService
) {

    @PostMapping("/request-membership")
    fun requestMembership(@RequestParam userId: Long, @RequestParam labId: Long): ResponseEntity<String> {
        labService.requestLabMembership(userId, labId)
        return ResponseEntity.ok("Membership request sent")
    }

    @PostMapping("/respond-to-request")
    fun respondToRequest(@RequestParam requestId: Long, @RequestParam accept: Boolean): ResponseEntity<String> {
        if (accept) {
            val success = labService.addUserToLab(requestId)
            if (success) {
                return ResponseEntity.ok("Membership accepted and user added to lab.")
            } else {
                return ResponseEntity.badRequest().body("Error adding user to lab.")
            }
        } else {
            val success = labService.removeMembershipRequest(requestId)
            if (success) {
                return ResponseEntity.ok("Membership request removed.")
            } else {
                return ResponseEntity.badRequest().body("Error removing membership request.")
            }
        }
    }

}
