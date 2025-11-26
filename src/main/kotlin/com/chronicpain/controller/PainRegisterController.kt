package com.chronicpain.controller

import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/pain")
class PainRegisterController(
    private val createPainRegisterUseCase: CreatePainRegisterUseCase,
    private val getUserPainHistoryUseCase: GetUserPainHistoryUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreatePainRegisterRequest): ResponseEntity<PainRegisterResponse> {
        return ResponseEntity.ok(createPainRegisterUseCase.execute(request))
    }

    @GetMapping("/{userId}")
    fun getById(@PathVariable userId: Long): ResponseEntity<PainRegisterResponse> {
        return ResponseEntity.ok(getUserPainHistoryUseCase.execute(userId))
    }
}