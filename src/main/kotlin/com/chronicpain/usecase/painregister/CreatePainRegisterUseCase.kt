package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.domain.model.User
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreatePainRegisterUseCase(
    private val painRegisterRepository: PainRegisterRepository,
    private val userRepository: UserRepository
) : UseCase<CreatePainRegisterRequest, PainRegisterResponse> {

    companion object {
        const val LOG_PREFIX = "[CREATE_PAIN_REGISTER_USE_CASE]"
    }

    override fun execute(input: CreatePainRegisterRequest): PainRegisterResponse {
        this.logger().info("$LOG_PREFIX Starting to create pain register: $input")

        val painRegister = buildPainRegister(input)

        val savedPainRegister = painRegisterRepository.save(painRegister)

        return buildPainRegisterResponse(savedPainRegister)
    }
    private fun buildPainRegister(request: CreatePainRegisterRequest): PainRegister {
        val user = fetchUserForPainRegister(request)

        return PainRegister(
            user = user,
            intensity = request.intensity,
            description = request.description,
            bodyPart = request.bodyPart,
            occurAt = request.occurAt ?: LocalDateTime.now()
        )
    }

    private fun fetchUserForPainRegister(request: CreatePainRegisterRequest): User =
        userRepository.findById(request.userId)
            .orElseThrow {
                val message = String.format("User ${request.userId} not found for register pain")
                this.logger().warn("{} {}", LOG_PREFIX, message)
                throw IllegalArgumentException(message)
            }


    private fun buildPainRegisterResponse(savedPainRegister: PainRegister): PainRegisterResponse =
        PainRegisterResponse(
            id = savedPainRegister.id,
            intensity = savedPainRegister.intensity,
            description = savedPainRegister.description,
            bodyPart = savedPainRegister.bodyPart,
            occurAt = savedPainRegister.occurAt
        )
}