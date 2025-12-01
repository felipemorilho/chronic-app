package com.chronicpain.domain.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 3, max = 70, message = "Name must be between 3 and 70 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "E-mail format is invalid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 15, message = "Password must be between 3 and 15 characters")
    val password: String,

    @field:NotBlank(message = "Type is required")
    @field:Size(min = 3, max = 15, message = "Type must be between 3 and 15 characters")
    val type: String
)
