package com.example.lsa.common.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
    fun sendEmail(to: String, subject: String, text: String) {
        val fromAddress = System.getenv("EMAIL_FROM_ADDRESS")
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(fromAddress)
        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(text, true)
        mailSender.send(message)
    }
}
