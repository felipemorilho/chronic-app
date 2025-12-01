package com.chronicpain.domain.dto.report

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import java.time.LocalDate

data class CreateReportRequest(

    @field:Positive(message = "UserId should be positive")
    val userId: Long,

    @field:NotNull(message = "Date of pain beginning is required")
    @field:PastOrPresent(message = "Date of pain cannot be in the future")
    val startedAt: LocalDate,

    @field:PastOrPresent(message = "End Date of pain cannot be in the future")
    val endedAt: LocalDate? = null,

    @field:NotNull(message = "Summary is required")
    val summary: String
)
