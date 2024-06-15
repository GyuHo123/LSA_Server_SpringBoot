package com.example.lsa.research.entity

import jakarta.persistence.*

@Entity
@Table(name = "virtual_object")
data class VirtualObject(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "text")
    val text: String? = null,

    @Column(name = "image_name")
    val imageName: String? = null,

    @Column(name = "sound_name")
    val soundName: String? = null,

    @Column(name = "manual_id", nullable = false)
    val manualId: Long,

    @ElementCollection
    @CollectionTable(name = "virtual_object_position", joinColumns = [JoinColumn(name = "virtual_object_id")])
    @Column(name = "position")
    val position: List<Int> = listOf(),

    @ElementCollection
    @CollectionTable(name = "virtual_object_size", joinColumns = [JoinColumn(name = "virtual_object_id")])
    @Column(name = "size")
    val size: List<Int> = listOf()
)
