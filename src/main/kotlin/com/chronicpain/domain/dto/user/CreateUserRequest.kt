package com.chronicpain.domain.dto.user

data class CreateUserRequest(

    val name: String,
    val email: String,
    val password: String,
    val type: String
)
