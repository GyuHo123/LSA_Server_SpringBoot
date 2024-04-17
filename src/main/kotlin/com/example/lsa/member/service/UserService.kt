package com.example.lsa.member.service

import com.example.lsa.member.dto.UserDto
import com.example.lsa.member.entity.User
import com.example.lsa.member.entity.*
import com.example.lsa.member.repo.UserRepository
import com.example.lsa.member.repo.LabRepository
import com.example.lsa.member.repo.StaffRepository
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
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun registerUser(userDto: UserDto): User {
        validateUser(userDto)

        staffRepository.findByStaffIdAndName(userDto.staffId, userDto.name) ?: throw IllegalArgumentException("No matching staff member found")

        val encodedPassword = passwordEncoder.encode(userDto.password)
        val labs = when (userDto.role) {
            "RESEARCHER" -> findLabsForResearcher(userDto.staffId)
            else -> setOf()  // 학생은 연구실을 null로 설정
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
    }

    private fun findLabsForResearcher(staffId: String): Set<Lab> {
        return labRepository.findAllByDirectorStaffId(staffId).toSet()
    }

    private fun validateUser(userDto: UserDto) {
        if (userRepository.existsByUsername(userDto.username)) {
            throw IllegalStateException("Username already exists: ${userDto.username}")
        }
    }

    fun getUserDetails(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User not found: $username")
    }
}
