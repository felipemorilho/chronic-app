package com.chronicpain.exception

import java.time.LocalDateTime

data class ApiError(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val message: String,
    val details: String? = null
)