package com.chronicpain.usecase.report

import com.chronicpain.domain.model.Report
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class DeleteReportUseCase(
    private val reportRepository: ReportRepository
) : UseCase<Long, Unit> {

    companion object {
        const val LOG_PREFIX = "[DELETE_REPORT_USE_CASE]"
    }

    override fun execute(input: Long) {
        this.logger().info("$LOG_PREFIX Starting to delete report with id $input")

        val existingReport = fetchReport(input)

        reportRepository.delete(existingReport)

        this.logger().info("$LOG_PREFIX Successfully deleted report with id $input")
    }

    private fun fetchReport(input: Long): Report =
        reportRepository.findById(input)
            .orElseThrow {
                val message = "Report with id $input not found"
                this.logger().warn("$LOG_PREFIX $message")
                throw NotFoundException(message)
            }

}