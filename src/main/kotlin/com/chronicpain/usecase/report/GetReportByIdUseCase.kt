package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.domain.model.Report
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetReportByIdUseCase(
    private val reportRepository: ReportRepository
) : UseCase<Long, ReportResponse> {

    companion object {
        const val LOG_PREFIX = "[GET_REPORT_BY_ID_USE_CASE]"
    }

    override fun execute(input: Long): ReportResponse {
        this.logger().info("$LOG_PREFIX Starting to fetch report by id=$input")

        val report = fetchReportById(input)

        return buildReportResponse(report)
    }

    private fun fetchReportById(id: Long): Report =
        reportRepository.findById(id)
            .orElseThrow {
                val msg = "Report $id not found"
                this.logger().warn("$LOG_PREFIX $msg")
                NotFoundException(msg)
            }

    private fun buildReportResponse(report: Report): ReportResponse =
        ReportResponse(
            id = report.id,
            userId = report.user.id,
            startedAt = report.startedAt,
            endedAt = report.endedAt,
            summary = report.summary,
            generatedAt = report.generatedAt
        )
}