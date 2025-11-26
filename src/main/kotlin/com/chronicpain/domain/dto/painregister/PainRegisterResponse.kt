package com.chronicpain.domain.dto.painregister

import java.time.LocalDateTime

data class PainRegisterResponse(

    val id: Long,
    val intensity: Int,
    val description: String?,
    val bodyPart: String?,
    val occurAt: LocalDateTime
)
