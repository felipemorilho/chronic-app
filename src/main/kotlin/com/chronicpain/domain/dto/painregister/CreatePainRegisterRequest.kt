package com.chronicpain.domain.dto.painregister

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreatePainRegisterRequest(

    @field:Positive(message = "UserId should be positive")
    val userId: Long,

    @field:NotBlank(message = "Name is required")
    @field:Min(value = 1, message = "Intensity minimum is 1")
    @field:Max(value = 10, message = "Intensity minimum is 10")
    val intensity: Int,

    @field:Size(max = 200, message = "Type must have maximum of 200 characters")
    val description: String? = null,

    val bodyPart: String?,

    @field:PastOrPresent(message = "Occur date cannot be in the future")
    val occurAt: LocalDateTime? = null
)
