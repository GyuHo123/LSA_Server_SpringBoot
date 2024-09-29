package com.example.lsa.portal.entity

import com.example.lsa.portal.dto.SharedManualDTO
import com.example.lsa.research.entity.Manual
import jakarta.persistence.*

@Entity
@Table(name = "shared_manual")
data class SharedManual(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "name", nullable = false)
    val name: String,
    @Column(name = "originLabId", nullable = false)
    val originLabId: Long,
    @Column(name = "originResearchId", nullable = false)
    val originResearchId: Long,
    @Column(name = "downloads", nullable = false)
    val downloads: Long,
)

fun SharedManual.toDTO(): SharedManualDTO {
    return SharedManualDTO(
            manualId = this.id,
            name = this.name,
            downloads = this.downloads
    )
}