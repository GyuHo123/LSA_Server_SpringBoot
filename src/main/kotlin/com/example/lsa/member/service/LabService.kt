package com.example.lsa.member.service

import com.example.lsa.member.dto.LabDto
import com.example.lsa.member.dto.LabMembershipRequestDto
import com.example.lsa.member.dto.UserInfoDto
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
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("사용자를 찾을 수 없습니다") }
        val lab = labRepository.findById(labId).orElseThrow { IllegalArgumentException("연구실을 찾을 수 없습니다.") }

        if (user.role == Role.STUDENT) {
            val request = LabMembershipRequest(user = user, lab = lab)
            requestRepository.save(request)
        } else {
            throw IllegalStateException("학생만 요청 가능합니다.")
        }
    }

    @Transactional
    fun addUserToLab(requestId: Long): Boolean {
        return try {
            val request = requestRepository.findById(requestId).orElseThrow { IllegalArgumentException("요청을 찾을 수 없습니다") }
            val newUserLab = UserLab(userId = request.user.id, labId = request.lab.id)
            userLabRepository.save(newUserLab)
            requestRepository.delete(request)
            true
        } catch (e: Exception) {
            println("오류 발생 : ${e.message}")
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
            println("오류 발생 : ${e.message}")
            false
        }
    }

    fun getMembershipRequestsByLab(labId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByLab_Id(labId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                staffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name,
                role = request.user.role.name,
                dept = request.user.dept,
                labDept = request.lab.dept,
                requestId = request.id
            )
        }
    }

    fun getMembershipRequestsByUser(userId: Long): List<LabMembershipRequestDto> {
        val requests = requestRepository.findAllByUser_Id(userId)
        return requests.map { request ->
            LabMembershipRequestDto(
                userName = request.user.name,
                staffId = request.user.staffId,
                labId = request.lab.id,
                labName = request.lab.name,
                role = request.user.role.name,
                dept = request.user.dept,
                labDept = request.lab.dept,
                requestId = request.id
            )
        }
    }

    fun getLabMembers(labId: Long): List<UserInfoDto> {
        val userLabs = userLabRepository.findAllByLabId(labId)
        return userLabs.map { userLab ->
            val user = userRepository.findById(userLab.userId).orElseThrow { IllegalArgumentException("사용자 정보가 없습니다") }
            UserInfoDto(
                userId = user.id,
                name = user.name,
                role = user.role.name,
                staffId = user.staffId,
                dept = user.dept
            )
        }
    }

    fun getLabsByUser(userId: Long): List<LabDto> {
        val userLabs = userLabRepository.findAllByUserId(userId)
        return userLabs.map { userLab ->
            val lab = labRepository.findById(userLab.labId).orElseThrow { IllegalArgumentException("연구실 정보가 없습니다") }
            LabDto(
                labId = lab.id,
                labName = lab.name,
                dept = lab.dept
            )
        }
    }

    fun findLabById(labId: Long): LabDto {
        val lab = labRepository.findById(labId).orElseThrow { IllegalArgumentException("연구실 정보가 없습니다") }
        return LabDto(
            labId = lab.id,
            labName = lab.name,
            dept = lab.dept
        )
    }

    fun removeUserFromLab(userId: Long, labId: Long): Boolean {
        return try {
            val userLab = userLabRepository.findByUserIdAndLabId(userId, labId)
                ?: throw IllegalArgumentException("유저가 연구실에 없습니다")
            userLabRepository.delete(userLab)
            true
        } catch (e: Exception) {
            println("오류 발생 : ${e.message}")
            false
        }
    }
}
