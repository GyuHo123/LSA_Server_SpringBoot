package com.example.lsa.member.service

import com.example.lsa.member.dto.LabDto
import com.example.lsa.member.dto.LabMembershipRequestDto
import com.example.lsa.member.dto.UserDto
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

    fun getMembershipRequestsByLab(labId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByLab_Id(labId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                userStaffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name,
                role = request.user.role.name,
                dept = request.user.dept,
                requestId = request.id
            )
        }
    }

    fun getMembershipRequestsByUser(userId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByUser_Id(userId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                userStaffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name,
                role = request.user.role.name,
                dept = request.user.dept,
                requestId = request.id
            )
        }
    }

    fun getLabMembers(labId: Long): List<UserDto> {
        val userLabs = userLabRepository.findAllByLabId(labId)
        return userLabs.map { userLab ->
            val user = userRepository.findById(userLab.userId).orElseThrow { IllegalArgumentException("User not found") }
            UserDto(
                username = user.username,
                password = "", // Don't expose the password
                role = user.role.name,
                labs = listOf(),
                labNames = listOf(),
                staffId = user.staffId,
                name = user.name,
                dept = user.dept
            )
        }
    }

    fun getLabsByUser(userId: Long): List<LabDto> {
        val userLabs = userLabRepository.findAllByUserId(userId)
        return userLabs.map { userLab ->
            val lab = labRepository.findById(userLab.labId).orElseThrow { IllegalArgumentException("Lab not found") }
            LabDto(
                labId = lab.id,
                labName = lab.name,
                dept = lab.dept
            )
        }
    }

    fun findLabById(labId: Long): LabDto {
        val lab = labRepository.findById(labId).orElseThrow { IllegalArgumentException("Lab not found") }
        return LabDto(
            labId = lab.id,
            labName = lab.name,
            dept = lab.dept
        )
    }

    fun removeUserFromLab(userId: Long, labId: Long): Boolean {
        return try {
            val userLab = userLabRepository.findByUserIdAndLabId(userId, labId)
                ?: throw IllegalArgumentException("Membership not found")
            userLabRepository.delete(userLab)
            true
        } catch (e: Exception) {
            println("Error removing user from lab: ${e.message}")
            false
        }
    }
}
