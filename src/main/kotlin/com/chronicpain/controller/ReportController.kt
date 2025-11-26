package com.chronicpain.controller

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chronicpain/v1/report")
class ReportController(
    private val createReporUseCase: CreateReporUseCase,
    private val getReportsByUserUseCase: GetReportsByUserUseCase
) {

    @PostMapping
    fun create(@RequestBody request: CreateReportRequest): ResponseEntity<ReportResponse> {
        return ResponseEntity.ok(createReporUseCase.execute(request))
    }

    @GetMapping("/{userId}")
    fun getById(@PathVariable userId: Long): ResponseEntity<List<ReportResponse>> {
        return ResponseEntity.ok(getReportsByUserUseCase.execute(userId))
    }
}