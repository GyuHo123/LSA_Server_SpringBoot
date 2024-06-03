package com.example.lsa.member.controller

import com.example.lsa.member.dto.LabDto
import com.example.lsa.member.dto.LabMembershipRequestDto
import com.example.lsa.member.dto.UserDto
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

    @GetMapping("/{labId}/membership-requests")
    fun getLabMembershipRequests(@PathVariable labId: Long): ResponseEntity<List<LabMembershipRequestDto>> {
        val requests = labService.getMembershipRequestsByLab(labId)
        return ResponseEntity.ok(requests)
    }

    @GetMapping("/user/{userId}/membership-requests")
    fun getUserMembershipRequests(@PathVariable userId: Long): ResponseEntity<List<LabMembershipRequestDto>> {
        val requests = labService.getMembershipRequestsByUser(userId)
        return ResponseEntity.ok(requests)
    }

    @GetMapping("/{labId}/find-membership")
    fun getLabMembers(@PathVariable labId: Long): ResponseEntity<List<UserDto>> {
        val members = labService.getLabMembers(labId)
        return ResponseEntity.ok(members)
    }

    @GetMapping("/user/{userId}/find-user-labs")
    fun getUserLabs(@PathVariable userId: Long): ResponseEntity<List<LabDto>> {
        val labs = labService.getLabsByUser(userId)
        return ResponseEntity.ok(labs)
    }

    @GetMapping("/{labId}/find-labs")
    fun getLabById(@PathVariable labId: Long): ResponseEntity<LabDto> {
        val lab = labService.findLabById(labId)
        return ResponseEntity.ok(lab)
    }

    @PostMapping("/remove-membership")
    fun removeMembership(@RequestParam userId: Long, @RequestParam labId: Long): ResponseEntity<String> {
        return if (labService.removeUserFromLab(userId, labId)) {
            ResponseEntity.ok("User removed from lab successfully")
        } else {
            ResponseEntity.badRequest().body("Error removing user from lab")
        }
    }
}
