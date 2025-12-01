package com.chronicpain.domain.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class UpdateUserRequest(

    val userId: Long,

    @field:Size(min = 3, max = 70, message = "Name must be between 3 and 70 characters")
    val name: String? = null,

    @field:Email(message = "E-mail format is invalid")
    val email: String? = null,

    @field:Size(min = 6, max = 15, message = "Password must be between 6 and 15 characters")
    val password: String? = null,

    @field:Size(min = 3, max = 15, message = "Type must be between 3 and 15 characters")
    val type: String? = null
)