package com.example.lsa.member.service

import com.example.lsa.common.service.EmailService
import com.example.lsa.member.dto.UserDto
import com.example.lsa.member.entity.User
import com.example.lsa.member.entity.*
import com.example.lsa.member.repo.UserRepository
import com.example.lsa.member.repo.LabRepository
import com.example.lsa.member.repo.StaffRepository
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.dao.DataIntegrityViolationException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val labRepository: LabRepository,
    private val staffRepository: StaffRepository,
    private val passwordEncoder: PasswordEncoder,
    private val verificationService: VerificationService,
    private val emailService: EmailService
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    @Transactional
    fun registerUser(userDto: UserDto) {
        try {
            validateUser(userDto)

            staffRepository.findByStaffIdAndName(userDto.staffId, userDto.name)
                ?: throw IllegalArgumentException("일치하는 인원 없음")

            val code = verificationService.generateAndSendVerificationCode(userDto.username)
            emailService.sendEmail(userDto.username, "LSA in JBNU 회원가입 인증 코드", "코드는 $code")

            throw IllegalStateException("인증메일이 전송되었으니 확인하고 입력해주세요")
        } catch (e: Exception) {
            logger.error("회원가입 중 오류 발생", e)
            throw e
        }
    }

    @Transactional
    fun completeRegistration(userDto: UserDto, code: String): User {
        try {
            if (!verificationService.verifyCode(userDto.username, code)) {
                throw IllegalArgumentException("인증번호 만료 / 일치 X")
            }

            val encodedPassword = passwordEncoder.encode(userDto.password)
            val labs = when (userDto.role) {
                "RESEARCHER" -> findLabsForResearcher(userDto.staffId).toMutableSet()
                else -> mutableSetOf()
            }

            val user = User(
                username = userDto.username,
                password = encodedPassword,
                role = Role.valueOf(userDto.role),
                labs = labs,
                staffId = userDto.staffId,
                name = userDto.name
            )

            try {
                return userRepository.save(user)
            } catch (e: DataIntegrityViolationException) {
                throw IllegalStateException("Duplicate username or other integrity issues", e)
            }
        } catch (e: Exception) {
            logger.error("Error during user registration completion", e)
            throw e
        }
    }

    private fun findLabsForResearcher(staffId: String): Set<Lab> {
        return labRepository.findAllByDirectorStaffId(staffId).toSet()
    }

    private fun validateUser(userDto: UserDto) {
        if (userRepository.existsByUsername(userDto.username)) {
            throw IllegalStateException("이메일이 존재합니다: ${userDto.username}")
        }
    }

    fun getUserByStaffId(staffId: String): User? {
        return userRepository.findByStaffId(staffId)
            ?: throw IllegalStateException("No user found with staffId: $staffId")
    }

    fun getUserDetails(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("이메일을 찾을 수 없습니다: $username")
    }
}
