package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetUserPainRegisterUseCase(
    private val painRegisterRepository: PainRegisterRepository
) : UseCase<Long, List<PainRegisterResponse>> {

    companion object {
        const val LOG_PREFIX = "[GET_PAIN_REGISTER_USE_CASE]"
    }

    override fun execute(input: Long): List<PainRegisterResponse> {
        this.logger().info("$LOG_PREFIX Starting to fetch pain register history for user: $input")

        val painRegisterList = painRegisterRepository.findByUserIdOrderByOccurAtDesc(input)
            .map {
                PainRegisterResponse(
                    id = it.id,
                    intensity = it.intensity,
                    description = it.description,
                    bodyPart = it.bodyPart,
                    occurAt = it.occurAt
                )
            }

        this.logger().info("$LOG_PREFIX Found ${painRegisterList.size} pain registers for user: $input")
        return painRegisterList
    }
}