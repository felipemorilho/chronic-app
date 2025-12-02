package com.chronicpain.usecase.report

import com.chronicpain.domain.model.Report
import com.chronicpain.domain.model.User
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DeleteReportUseCaseTest {

    @Mock
    lateinit var reportRepository: ReportRepository

    @InjectMocks
    lateinit var deleteReportUseCase: DeleteReportUseCase

    private val user = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    private val report = Report(
        id = 5L,
        user = user,
        startedAt = java.time.LocalDate.now(),
        endedAt = java.time.LocalDate.now(),
        summary = "Summary"
    )

    @Test
    fun `should delete report successfully`() {
        `when`(reportRepository.findById(5L))
            .thenReturn(Optional.of(report))

        deleteReportUseCase.execute(5L)

        verify(reportRepository).findById(5L)
        verify(reportRepository).delete(report)
    }

    @Test
    fun `should throw NotFoundException when report does not exist`() {
        `when`(reportRepository.findById(999L)).thenReturn(Optional.empty())

        val ex = assertThrows(NotFoundException::class.java) {
            deleteReportUseCase.execute(999L)
        }

        assertEquals("Report with id 999 not found", ex.message)

        verify(reportRepository).findById(999L)
        verify(reportRepository, never()).delete(any())
    }
}
