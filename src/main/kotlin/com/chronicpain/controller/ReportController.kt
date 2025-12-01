package com.chronicpain.controller

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.usecase.report.CreateReportUseCase
import com.chronicpain.usecase.report.GetUserReportsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/report")
class ReportController(
    private val createReportUseCase: CreateReportUseCase,
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
}