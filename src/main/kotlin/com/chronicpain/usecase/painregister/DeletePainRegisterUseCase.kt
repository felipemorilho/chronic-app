package com.chronicpain.usecase.painregister

import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class DeletePainRegisterUseCase(
    private val painRegisterRepository: PainRegisterRepository
) : UseCase<Long, Unit> {

    companion object {
        const val LOG_PREFIX = "[DELETE_PAIN_REGISTER_USE_CASE]"
    }

    override fun execute(input: Long) {
        this.logger().info("$LOG_PREFIX Starting to delete pain register with id $input")

        val existingPainRegister = fetchPainRegister(input)

        painRegisterRepository.delete(existingPainRegister)

        this.logger().info("$LOG_PREFIX Successfully deleted pain register with id $input")
    }

    private fun fetchPainRegister(id: Long) =
        painRegisterRepository.findById(id)
            .orElseThrow {
                val message = "Pain register $id not found"
                this.logger().warn("$LOG_PREFIX $message")
                NotFoundException(message)
            }
}