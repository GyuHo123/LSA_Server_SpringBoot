package com.example.lsa.member.service

import com.example.lsa.member.entity.*
import com.example.lsa.member.repo.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.lsa.member.entity.UserLab

@Service
class LabService(
    private val userRepository: UserRepository,
    private val labRepository: LabRepository,
    private val requestRepository: LabMembershipRequestRepository,
    private val userLabRepository: UserLabRepository
) {

    @Transactional
    fun requestLabMembership(userId: Long, labId: Long) {
        val user = userRepository.findByStaffId(userId.toString()) ?: throw IllegalArgumentException("User not found")
        val lab = labRepository.findById(labId).orElseThrow { IllegalArgumentException("Lab not found") }

        if (user.role == Role.STUDENT) {
            val request = LabMembershipRequest(user = user.staffId.toLong(), lab = lab.id)
            requestRepository.save(request)
        } else {
            throw IllegalStateException("Only students can request lab membership")
        }
    }

    @Transactional
    fun addUserToLab(requestId: Long): Boolean {
        return try {
            val request = requestRepository.findById(requestId).orElseThrow { IllegalArgumentException("Request not found") }
            val newUserLab = UserLab(userId = request.user, labId = request.lab)
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
}