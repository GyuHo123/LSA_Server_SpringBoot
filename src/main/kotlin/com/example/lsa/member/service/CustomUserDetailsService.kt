package com.example.lsa.member.service

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.example.lsa.member.repo.UserRepository
import org.springframework.security.core.userdetails.UserDetails

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username)?.let {
            CustomUserDetails(it)
        } ?: throw UsernameNotFoundException("일치하는 이메일이 없습니다: $username")
    }
}
