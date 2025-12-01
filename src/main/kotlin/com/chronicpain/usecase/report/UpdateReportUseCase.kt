package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.UpdateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.domain.model.Report
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class UpdateReportUseCase(
    private val reportRepository: ReportRepository
) : UseCase<UpdateReportRequest, ReportResponse> {

    companion object {
        const val LOG_PREFIX = "[UPDATE_REPORT_USE_CASE]"
    }

    override fun execute(input: UpdateReportRequest): ReportResponse {
        this.logger().info("$LOG_PREFIX Starting report update for id: ${input.reportId}, request: $input")

        val existingReport = fetchExistingReport(input.reportId)

        validateInput(input, existingReport)

        val updatedReport = updateFields(existingReport, input)
        val savedReport = reportRepository.save(updatedReport)

        return buildReportResponse(savedReport)
    }

    private fun fetchExistingReport(id: Long): Report =
        reportRepository.findById(id)
            .orElseThrow {
                val message = "Report not found for id: $id"
                this.logger().warn("$LOG_PREFIX $message")
                NotFoundException(message)
            }

    private fun validateInput(request: UpdateReportRequest, existing: Report) {
        if (request.startedAt == null &&
            request.endedAt == null &&
            request.summary == null
        ) {
            throw BadRequestException("At least one field must be provided to update the report")
        }

        request.startedAt?.let { newStart ->
            validateStartDate(newStart)
            request.endedAt?.let { newEnd ->
                validateDateOrder(newStart, newEnd)
            }
        }

        request.endedAt?.let { newEnd ->
            validateEndDate(newEnd)
            val start = request.startedAt ?: existing.startedAt
            validateDateOrder(start, newEnd)
        }

        request.summary?.let { validateSummary(it) }
    }

    private fun validateStartDate(startedAt: LocalDate) {
        if (startedAt.isAfter(LocalDate.now())) {
            throw BadRequestException("Start date cannot be in the future")
        }
    }

    private fun validateEndDate(endedAt: LocalDate) {
        if (endedAt.isAfter(LocalDate.now())) {
            throw BadRequestException("End date cannot be in the future")
        }
    }

    private fun validateDateOrder(startedAt: LocalDate, endedAt: LocalDate) {
        if (startedAt.isBefore(endedAt)) {
            throw BadRequestException("Started date must be greater than or equal to ended date")
        }
    }

    private fun validateSummary(summary: String) {
        if (summary.length < 3 || summary.length > 500)
            throw BadRequestException("Summary should have between 3 and 500 characters")
    }

    private fun updateFields(existing: Report, request: UpdateReportRequest): Report =
        existing.copy(
            startedAt = request.startedAt ?: existing.startedAt,
            endedAt = request.endedAt ?: existing.endedAt,
            summary = request.summary ?: existing.summary
        )

    private fun buildReportResponse(report: Report): ReportResponse =
        ReportResponse(
            id = report.id,
            startedAt = report.startedAt,
            endedAt = report.endedAt,
            summary = report.summary,
            sendAt = report.sendAt
        )
}