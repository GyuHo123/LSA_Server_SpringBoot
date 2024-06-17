package com.example.lsa.lab.service

import com.example.lsa.lab.entity.LabMembershipRequest
import com.example.lsa.lab.repo.LabMembershipRequestRepository
import com.example.lsa.lab.repo.LabRepository
import com.example.lsa.lab.dto.LabDto
import com.example.lsa.lab.dto.LabMembershipRequestDto
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

        val existingUserLab = userLabRepository.findByUserIdAndLabId(userId, labId)

        if(existingUserLab != null){
            throw IllegalStateException("이미 가입 중인 연구실입니다.")
        }

        val existingRequest = requestRepository.findByUserIdAndLabId(userId, labId)

        if (existingRequest == null) {
            val request = LabMembershipRequest(user = user, lab = lab)
            requestRepository.save(request)
        } else {
            throw IllegalStateException("이미 가입 요청이 있습니다: 사용자 ID = ${user.id}, 연구실 ID = ${lab.id}")
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
