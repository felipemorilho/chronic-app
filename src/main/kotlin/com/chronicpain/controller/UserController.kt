package com.chronicpain.controller

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(createUserUseCase.execute(request))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(createUserUseCase.execute(id))
    }
}