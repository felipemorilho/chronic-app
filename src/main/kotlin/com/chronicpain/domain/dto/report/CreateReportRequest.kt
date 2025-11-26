package com.chronicpain.domain.dto.report

import java.time.LocalDate

data class CreateReportRequest(

    val userId: Long,
    val startedAt: LocalDate,
    val endedAt: LocalDate? = null
)
