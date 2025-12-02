package com.chronicpain.usecase.report

import com.chronicpain.domain.model.Report
import com.chronicpain.domain.model.User
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class GetReportByIdUseCaseTest {

    @Mock
    lateinit var reportRepository: ReportRepository

    @InjectMocks
    lateinit var getReportByIdUseCase: GetReportByIdUseCase

    private val user = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    private val report = Report(
        id = 10L,
        user = user,
        startedAt = LocalDate.of(2025, 1, 1),
        endedAt = LocalDate.of(2025, 1, 2),
        summary = "Report summary"
    )

    @Test
    fun `should return report response when report exists`() {
        `when`(reportRepository.findById(10L))
            .thenReturn(Optional.of(report))

        val response = getReportByIdUseCase.execute(10L)

        assertEquals(10L, response.id)
        assertEquals(1L, response.userId)
        assertEquals("Report summary", response.summary)

        verify(reportRepository).findById(10L)
    }

    @Test
    fun `should throw NotFoundException when report is not found`() {
        `when`(reportRepository.findById(99L))
            .thenReturn(Optional.empty())

        val ex = assertThrows(NotFoundException::class.java) {
            getReportByIdUseCase.execute(99L)
        }

        assertEquals("Report 99 not found", ex.message)

        verify(reportRepository).findById(99L)
    }
}
