package com.chronicpain.domain.dto.painregister

import java.time.LocalDateTime

data class CreatePainRegisterRequest(

    val userId: Long,
    val intensity: Int,
    val description: String? = null,
    val bodyPart: String?,
    val occurAt: LocalDateTime? = null
)
