package com.chronicpain.repository

import com.chronicpain.domain.model.Report
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository : JpaRepository<Report, Long> {

    fun findByUserId(userId: Long): List<Report>
}