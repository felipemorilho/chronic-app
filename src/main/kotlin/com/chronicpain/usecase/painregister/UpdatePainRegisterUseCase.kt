package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.UpdatePainRegisterRequest
import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class UpdatePainRegisterUseCase(
    private val painRegisterRepository: PainRegisterRepository
) : UseCase<UpdatePainRegisterRequest, PainRegisterResponse> {

    companion object {
        const val LOG_PREFIX = "[UPDATE_PAIN_REGISTER_USE_CASE]"
    }

    override fun execute(input: UpdatePainRegisterRequest): PainRegisterResponse {
        this.logger().info("$LOG_PREFIX Starting to update pain register for id: ${input.painRegisterId}, request: $input")

        val existing = fetchPainRegister(input.painRegisterId)

        validateInput(input)

        val updated = updateFields(existing, input)
        val saved = painRegisterRepository.save(updated)

        return buildPainRegisterResponse(saved)
    }

    private fun fetchPainRegister(id: Long): PainRegister =
        painRegisterRepository.findById(id)
            .orElseThrow {
                val message = "Pain Register $id not found"
                this.logger().warn("$LOG_PREFIX $message")
                NotFoundException(message)
            }

    private fun validateInput(request: UpdatePainRegisterRequest) {

        if (request.intensity == null &&
            request.description == null &&
            request.bodyPart == null &&
            request.occurAt == null
        ) {
            throw BadRequestException("At least one field must be provided to update the pain register")
        }

        request.intensity?.let { validateIntensity(it) }

        request.occurAt?.let { occurAt ->
            if (occurAt.isAfter(java.time.LocalDateTime.now()))
                throw BadRequestException("Occur date cannot be in the future")
        }
    }

    private fun validateIntensity(intensity: Int) {
        if (intensity !in 1..10)
            throw BadRequestException("Intensity must be between 1 and 10")
    }

    private fun updateFields(existing: PainRegister, request: UpdatePainRegisterRequest): PainRegister =
        existing.copy(
            intensity = request.intensity ?: existing.intensity,
            description = request.description ?: existing.description,
            bodyPart = request.bodyPart ?: existing.bodyPart,
            occurAt = request.occurAt ?: existing.occurAt
        )

    private fun buildPainRegisterResponse(savedPainRegister: PainRegister): PainRegisterResponse =
        PainRegisterResponse(
            id = savedPainRegister.id,
            userId = savedPainRegister.user.id,
            intensity = savedPainRegister.intensity,
            description = savedPainRegister.description,
            bodyPart = savedPainRegister.bodyPart,
            occurAt = savedPainRegister.occurAt
        )
}