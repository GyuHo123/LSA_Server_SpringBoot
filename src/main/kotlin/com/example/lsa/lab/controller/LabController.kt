package com.example.lsa.lab.controller

import com.example.lsa.lab.dto.LabDto
import com.example.lsa.lab.dto.LabMembershipRequestDto
import com.example.lsa.member.dto.UserInfoDto
import com.example.lsa.lab.service.LabService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/labs")
class LabController(
    private val labService: LabService
) {

    @PostMapping("/request-membership")
    fun requestMembership(@RequestParam userId: Long, @RequestParam labId: Long): ResponseEntity<String> {
        return try {
            labService.requestLabMembership(userId, labId)
            ResponseEntity.ok("멤버쉽 요청이 성공적으로 처리되었습니다.")
        }catch(e: IllegalStateException){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }

    @PostMapping("/respond-to-request")
    fun respondToRequest(@RequestParam requestId: Long, @RequestParam accept: Boolean): ResponseEntity<String> {
        if (accept) {
            val success = labService.addUserToLab(requestId)
            if (!success) {
                return ResponseEntity.badRequest().body("오류 발생 : 멤버를 추가할 수 없습니다")
            }
            return ResponseEntity.ok("성공적으로 멤버를 추가했습니다")
        } else {
            val success = labService.removeMembershipRequest(requestId)
            if (!success) {
                return ResponseEntity.badRequest().body("오류가 발생했습니다")
            }
            return ResponseEntity.ok("성공적으로 요청이 거부되었습니다")
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
    fun getLabMembers(@PathVariable labId: Long): ResponseEntity<List<UserInfoDto>> {
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

    @DeleteMapping("/remove-membership")
    fun removeMembership(@RequestParam userId: Long, @RequestParam labId: Long): ResponseEntity<String> {
        return if (labService.removeUserFromLab(userId, labId)) {
            ResponseEntity.ok("성공적으로 삭제했습니다")
        } else {
            ResponseEntity.badRequest().body("오류가 발생했습니다.")
        }
    }
}
