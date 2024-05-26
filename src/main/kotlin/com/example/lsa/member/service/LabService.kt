package com.example.lsa.member.service

import com.example.lsa.member.dto.LabMembershipRequestDto
import com.example.lsa.member.entity.*
import com.example.lsa.member.repo.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class LabService(
    private val userRepository: UserRepository,
    private val labRepository: LabRepository,
    private val requestRepository: LabMembershipRequestRepository,
    private val userLabRepository: UserLabRepository,
) {

    @Transactional
    fun requestLabMembership(userId: Long, labId: Long) {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val lab = labRepository.findById(labId).orElseThrow { IllegalArgumentException("Lab not found") }

        if (user.role == Role.STUDENT) {
            val request = LabMembershipRequest(user = user, lab = lab)
            requestRepository.save(request)
        } else {
            throw IllegalStateException("Only students can request lab membership")
        }
    }

    @Transactional
    fun addUserToLab(requestId: Long): Boolean {
        return try {
            val request = requestRepository.findById(requestId).orElseThrow { IllegalArgumentException("Request not found") }
            val newUserLab = UserLab(userId = request.user.id, labId = request.lab.id)
            userLabRepository.save(newUserLab)
            requestRepository.delete(request)
            true
        } catch (e: Exception) {
            println("Error adding user to lab: ${e.message}")
            false
        }
    }

    @Transactional
    fun removeMembershipRequest(requestId: Long): Boolean {
        return try {
            val request = requestRepository.findById(requestId).orElseThrow { IllegalArgumentException("Request not found") }
            requestRepository.delete(request)
            true
        } catch (e: Exception) {
            println("Error removing membership request: ${e.message}")
            false
        }
    }

    // 특정 labId에 해당하는 연구실의 모든 요청을 가져오는 함수
    fun getMembershipRequestsByLab(labId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByLab_Id(labId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                userStaffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name
            )
        }
    }

    // 특정 userId에 해당하는 유저가 한 모든 요청을 가져오는 함수
    fun getMembershipRequestsByUser(userId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByUser_Id(userId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                userStaffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name
            )
        }
    }
}
