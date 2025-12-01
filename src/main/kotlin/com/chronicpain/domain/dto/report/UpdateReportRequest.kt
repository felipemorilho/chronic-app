package com.chronicpain.domain.dto.report

import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class UpdateReportRequest (

    val reportId: Long,

    @field:PastOrPresent(message = "Start date cannot be in the future")
    val startedAt: LocalDate? = null,

    @field:PastOrPresent(message = "End date cannot be in the future")
    val endedAt: LocalDate? = null,

    @field:Size(min = 3, max = 2000, message = "Summary must be between 3 and 2000 characters")
    val summary: String? = null
)