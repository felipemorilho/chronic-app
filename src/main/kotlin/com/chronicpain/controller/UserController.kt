package com.chronicpain.controller

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.dto.user.UpdateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.usecase.user.CreateUserUseCase
import com.chronicpain.usecase.user.GetUserUseCase
import com.chronicpain.usecase.user.UpdateUserUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/users")
class UserController(
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(createUserUseCase.execute(request))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(getUserUseCase.execute(id))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UpdateUserRequest): ResponseEntity<UserResponse> {
        val updateUserRequest = UpdateUserRequest(
            userId = id,
            name = request.name,
            email = request.email,
            password = request.password,
            type = request.type
        )

        return ResponseEntity.ok(updateUserUseCase.execute(updateUserRequest))
    }
}