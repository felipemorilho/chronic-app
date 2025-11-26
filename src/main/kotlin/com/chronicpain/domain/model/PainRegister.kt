package com.chronicpain.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pain_register")
data class PainRegister(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pain_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false)
    var intensity: Int,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "body_part", length = 100)
    var bodyPart: String? = null,

    @Column(name = "occur_at")
    var occurAt: LocalDateTime = LocalDateTime.now()
)
