package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.UpdateReportRequest
import com.chronicpain.domain.model.Report
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class UpdateReportUseCaseTest {

    @Mock
    lateinit var reportRepository: ReportRepository

    @InjectMocks
    lateinit var updateReportUseCase: UpdateReportUseCase

    private val user = User(
        id = 1L,
        name = "User",
        email = "user@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    private val existingReport = Report(
        id = 10L,
        user = user,
        startedAt = LocalDate.of(2025, 1, 10),
        endedAt = LocalDate.of(2025, 1, 10),
        summary = "Old summary"
    )

    @Test
    fun `should update report successfully`() {
        val request = UpdateReportRequest(
            reportId = 10L,
            startedAt = LocalDate.of(2025, 1, 15),
            endedAt = LocalDate.of(2025, 1, 15),
            summary = "Updated summary"
        )

        val updatedReport = existingReport.copy(
            startedAt = request.startedAt!!,
            endedAt = request.endedAt!!,
            summary = request.summary!!
        )

        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(existingReport))
        `when`(reportRepository.save(any(Report::class.java)))
            .thenReturn(updatedReport)

        val response = updateReportUseCase.execute(request)

        assertEquals(10L, response.id)
        assertEquals("Updated summary", response.summary)
        assertEquals(LocalDate.of(2025, 1, 15), response.startedAt)

        verify(reportRepository).findById(10L)
        verify(reportRepository).save(any(Report::class.java))
    }

    @Test
    fun `should throw NotFoundException when report does not exist`() {
        val request = UpdateReportRequest(
            reportId = 999L,
            summary = "Anything"
        )

        `when`(reportRepository.findById(999L))
            .thenReturn(Optional.empty())

        val ex = assertThrows(NotFoundException::class.java) {
            updateReportUseCase.execute(request)
        }

        assertEquals("Report not found for id: 999", ex.message)

        verify(reportRepository).findById(999L)
        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when no fields are provided`() {
        val request = UpdateReportRequest(
            reportId = 10L,
            startedAt = null,
            endedAt = null,
            summary = null
        )

        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(existingReport))

        val ex = assertThrows(BadRequestException::class.java) {
            updateReportUseCase.execute(request)
        }

        assertEquals("At least one field must be provided to update the report", ex.message)

        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when updating start date to future`() {
        val request = UpdateReportRequest(
            reportId = 10L,
            startedAt = LocalDate.now().plusDays(1)
        )

        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(existingReport))

        val ex = assertThrows(BadRequestException::class.java) {
            updateReportUseCase.execute(request)
        }

        assertEquals("Start date cannot be in the future", ex.message)

        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when summary is invalid`() {
        val request = UpdateReportRequest(
            reportId = 10L,
            summary = "ok"
        )

        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(existingReport))

        val ex = assertThrows(BadRequestException::class.java) {
            updateReportUseCase.execute(request)
        }

        assertEquals("Summary should have between 3 and 500 characters", ex.message)

        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when date order is invalid`() {
        val request = UpdateReportRequest(
            reportId = 10L,
            startedAt = LocalDate.of(2025, 1, 1),
            endedAt = LocalDate.of(2025, 1, 20)
        )

        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(existingReport))

        val ex = assertThrows(BadRequestException::class.java) {
            updateReportUseCase.execute(request)
        }

        assertEquals("Started date must be greater than or equal to ended date", ex.message)

        verify(reportRepository, never()).save(any())
    }
}
