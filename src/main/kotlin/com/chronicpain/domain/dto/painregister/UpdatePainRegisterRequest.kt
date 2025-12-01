package com.chronicpain.domain.dto.painregister

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UpdatePainRegisterRequest(

    val painRegisterId: Long,

    @field:Min(value = 1, message = "Intensity minimum is 1")
    @field:Max(value = 10, message = "Intensity maximum is 10")
    val intensity: Int? = null,

    @field:Size(max = 200, message = "Description must have maximum of 200 characters")
    val description: String? = null,

    val bodyPart: String? = null,

    @field:PastOrPresent(message = "Occur date cannot be in the future")
    val occurAt: LocalDateTime? = null
)