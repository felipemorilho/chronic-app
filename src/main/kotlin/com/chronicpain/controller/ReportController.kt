package com.chronicpain.controller

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.domain.dto.report.UpdateReportRequest
import com.chronicpain.usecase.report.CreateReportUseCase
import com.chronicpain.usecase.report.DeleteReportUseCase
import com.chronicpain.usecase.report.GetUserReportsUseCase
import com.chronicpain.usecase.report.UpdateReportUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/report")
class ReportController(
    private val createReportUseCase: CreateReportUseCase,
    private val updateReportUseCase: UpdateReportUseCase,
    private val deleteReportUseCase: DeleteReportUseCase,
    private val getUserReportsUseCase: GetUserReportsUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreateReportRequest): ResponseEntity<ReportResponse> {
        return ResponseEntity.ok(createReportUseCase.execute(request))
    }

    @GetMapping("/{userId}")
    fun getById(@PathVariable userId: Long): ResponseEntity<List<ReportResponse>> {
        return ResponseEntity.ok(getUserReportsUseCase.execute(userId))
    }

    @PutMapping("/{reportId}")
    fun update(@PathVariable reportId: Long, @RequestBody request: UpdateReportRequest): ResponseEntity<ReportResponse> {
        val updateReportRequest = UpdateReportRequest(
            reportId = reportId,
            startedAt = request.startedAt,
            endedAt = request.endedAt,
            summary = request.summary
        )

        return ResponseEntity.ok(updateReportUseCase.execute(updateReportRequest))
    }

    @DeleteMapping("/{reportId}")
    fun delete(@PathVariable reportId: Long): ResponseEntity<Unit> {
        return ResponseEntity.ok(deleteReportUseCase.execute(reportId))
    }
}