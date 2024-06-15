package com.example.lsa.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths

@Configuration
class FileUploadConfig(
    @Value("\${file.upload-dir}")
    private val uploadDir: String
) {
    @Bean
    fun init() = CommandLineRunner {
        Files.createDirectories(Paths.get(uploadDir))
    }
}
