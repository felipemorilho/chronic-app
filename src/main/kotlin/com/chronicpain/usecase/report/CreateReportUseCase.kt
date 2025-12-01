package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.domain.model.Report
import com.chronicpain.domain.model.User
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class CreateReportUseCase(
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository
) : UseCase<CreateReportRequest, ReportResponse> {

    companion object {
        const val LOG_PREFIX = "[CREATE_REPORT_USE_CASE]"
    }

    override fun execute(input: CreateReportRequest): ReportResponse {
        this.logger().info("$LOG_PREFIX Starting to create report: $input")

        val report = buildReport(input)
        val savedReport = reportRepository.save(report)

        return buildReportResponse(savedReport)
    }

    private fun buildReport(request: CreateReportRequest): Report {
        val user = fetchReportUser(request.userId)
        return Report(
            user = user,
            startedAt = request.startedAt,
            endedAt = request.endedAt,
            summary = request.summary
        )
    }

    private fun fetchReportUser(id: Long): User =
        userRepository.findById(id)
            .orElseThrow {
                val message = String.format("User not found for id: $id")
                this.logger().warn("{} {}", LOG_PREFIX, message)
                NotFoundException(message)
            }

    private fun buildReportResponse(savedReport: Report): ReportResponse {
        return ReportResponse(
            id = savedReport.id,
            startedAt = savedReport.startedAt,
            endedAt = savedReport.endedAt,
            summary = savedReport.summary,
            sendAt = savedReport.sendAt
        )
    }
}