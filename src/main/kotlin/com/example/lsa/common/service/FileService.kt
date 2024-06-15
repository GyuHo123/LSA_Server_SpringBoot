package com.example.lsa.common.service

import com.example.lsa.common.dto.FileUploadResponse
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileService {

    private val fileStorageLocation: Path = Paths.get("uploads").toAbsolutePath().normalize()

    init {
        Files.createDirectories(fileStorageLocation)
    }

    fun uploadFile(file: MultipartFile, manualId: Long, virtualObjectId: Long): FileUploadResponse {
        val fileNameBase = "${manualId}_${virtualObjectId}"
        val extension = file.originalFilename?.substringAfterLast('.', "") ?: ""
        var fileName = if (extension.isNotBlank()) "$fileNameBase.$extension" else fileNameBase
        var targetLocation = fileStorageLocation.resolve(fileName)
        var counter = 1

        while (Files.exists(targetLocation)) {
            fileName = if (extension.isNotBlank()) "$fileNameBase" + "_" +"$counter.$extension" else "${fileNameBase}_$counter"
            targetLocation = fileStorageLocation.resolve(fileName)
            counter++
        }

        Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        return FileUploadResponse(fileName = fileName, fileUrl = "/api/research/vobject/files/$fileName")
    }

    fun loadFileAsResource(fileName: String): Resource {
        val filePath = fileStorageLocation.resolve(fileName).normalize()
        return FileSystemResource(filePath)
    }
}
