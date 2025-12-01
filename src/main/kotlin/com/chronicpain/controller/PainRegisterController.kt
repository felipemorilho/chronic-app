package com.chronicpain.controller

import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.domain.dto.painregister.UpdatePainRegisterRequest
import com.chronicpain.usecase.painregister.CreatePainRegisterUseCase
import com.chronicpain.usecase.painregister.DeletePainRegisterUseCase
import com.chronicpain.usecase.painregister.GetPainRegisterByIdUseCase
import com.chronicpain.usecase.painregister.GetUserPainRegisterUseCase
import com.chronicpain.usecase.painregister.UpdatePainRegisterUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chronicpain/v1/pain")
class PainRegisterController(
    private val createPainRegisterUseCase: CreatePainRegisterUseCase,
    private val updatePainRegisterUseCase: UpdatePainRegisterUseCase,
    private val deletePainRegisterUseCase: DeletePainRegisterUseCase,
    private val getUserPainRegisterUseCase: GetUserPainRegisterUseCase,
    private val getPainRegisterByIdUseCase: GetPainRegisterByIdUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreatePainRegisterRequest): ResponseEntity<PainRegisterResponse> {
        return ResponseEntity.ok(createPainRegisterUseCase.execute(request))
    }

    @GetMapping("/{painRegisterId}")
    fun getById(@PathVariable painRegisterId: Long): ResponseEntity<PainRegisterResponse> {
        return ResponseEntity.ok(getPainRegisterByIdUseCase.execute(painRegisterId))
    }

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: Long): ResponseEntity<List<PainRegisterResponse>> {
        return ResponseEntity.ok(getUserPainRegisterUseCase.execute(userId))
    }

    @PutMapping("/{painRegisterId}")
    fun update(@PathVariable painRegisterId: Long, @RequestBody request: UpdatePainRegisterRequest): ResponseEntity<PainRegisterResponse> {
        val updatePainRegisterRequest = UpdatePainRegisterRequest(
            painRegisterId = painRegisterId,
            intensity = request.intensity,
            description = request.description,
            bodyPart = request.bodyPart,
            occurAt = request.occurAt
        )

        return ResponseEntity.ok(updatePainRegisterUseCase.execute(updatePainRegisterRequest))
    }

    @DeleteMapping("/{painRegisterId}")
    fun delete(@PathVariable painRegisterId: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(deletePainRegisterUseCase.execute(painRegisterId))
    }

}