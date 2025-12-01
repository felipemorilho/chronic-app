package com.chronicpain.domain.dto.report

import java.time.LocalDate
import java.time.LocalDateTime

data class ReportResponse(

    val id: Long,
    val userId: Long,
    val startedAt: LocalDate,
    val endedAt: LocalDate? = null,
    val summary: String?,
    val generatedAt: LocalDateTime
)
