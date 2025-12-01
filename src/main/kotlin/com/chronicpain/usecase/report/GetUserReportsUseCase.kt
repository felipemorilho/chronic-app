package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.repository.ReportRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetUserReportsUseCase(
    private val reportRepository: ReportRepository
) :UseCase<Long, List<ReportResponse>> {

    companion object {
        const val LOG_PREFIX = "[GET_REGISTER_USE_CASE]"
    }

    override fun execute(input: Long): List<ReportResponse> {
        this.logger().info("$LOG_PREFIX Starting to get reports for user: $input")

        return reportRepository.findByUserId(input)
            .map {
                ReportResponse(
                    id = it.id,
                    startedAt = it.startedAt,
                    endedAt = it.endedAt,
                    summary = it.summary,
                    sendAt = it.sendAt
                )
            }
    }
}