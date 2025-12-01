package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetPainRegisterByIdUseCase(
    private val painRegisterRepository: PainRegisterRepository
) : UseCase<Long, PainRegisterResponse> {

    companion object {
        const val LOG_PREFIX = "[GET_REPORT_BY_ID_USE_CASE]"
    }

    override fun execute(input: Long): PainRegisterResponse {
        this.logger().info("$LOG_PREFIX Starting to fetch pain register for id $input")

        val report = fetchPainRegisterById(input)

        return buildPainRegisterResponse(report)
    }

    private fun fetchPainRegisterById(id: Long): PainRegister =
        painRegisterRepository.findById(id)
            .orElseThrow {
                val msg = "Pain Register $id not found"
                this.logger().warn("$LOG_PREFIX $msg")
                NotFoundException(msg)
            }

    private fun buildPainRegisterResponse(register: PainRegister): PainRegisterResponse =
        PainRegisterResponse(
            id = register.id,
            userId = register.user.id,
            intensity = register.intensity,
            description = register.description,
            bodyPart = register.bodyPart,
            occurAt = register.occurAt
        )
}