package com.chronicpain.domain.model

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "report")
data class Report(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "started_at", nullable = false)
    var startedAt: LocalDate,

    @Column(name = "ended_at")
    var endedAt: LocalDate?,

    @Column(columnDefinition = "TEXT")
    var summary: String? = null,

    @Column(name = "generated_at")
    var generatedAt: LocalDateTime = LocalDateTime.now()
)
