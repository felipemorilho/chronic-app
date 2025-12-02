package com.chronicpain.usecase.report

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.model.Report
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.ReportRepository
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class CreateReportUseCaseTest {

    @Mock
    lateinit var reportRepository: ReportRepository

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var createReportUseCase: CreateReportUseCase

    private val user = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    @Test
    fun `should create report successfully`() {
        val request = CreateReportRequest(
            userId = 1L,
            startedAt = LocalDate.of(2025, 1, 10),
            endedAt = LocalDate.of(2025, 1, 10),
            summary = "Strong pain in the lower back"
        )

        val report = Report(
            id = 10L,
            user = user,
            startedAt = request.startedAt,
            endedAt = request.endedAt,
            summary = request.summary
        )

        `when`(userRepository.findById(1L))
            .thenReturn(java.util.Optional.of(user))
        `when`(reportRepository.save(any(Report::class.java)))
            .thenReturn(report)

        val response = createReportUseCase.execute(request)

        assertEquals(10L, response.id)
        assertEquals(1L, response.userId)
        assertEquals("Strong pain in the lower back", response.summary)

        verify(userRepository).findById(1L)
        verify(reportRepository).save(any(Report::class.java))
    }

    @Test
    fun `should throw NotFoundException when user does not exist`() {
        val request = CreateReportRequest(
            userId = 99L,
            startedAt = LocalDate.of(2025, 1, 10),
            endedAt = LocalDate.of(2025, 1, 10),
            summary = "Some summary"
        )

        `when`(userRepository.findById(99L))
            .thenReturn(java.util.Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            createReportUseCase.execute(request)
        }

        assertEquals("User not found for id: 99", exception.message)

        verify(userRepository).findById(99L)
        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when summary is too short`() {
        val request = CreateReportRequest(
            userId = 1L,
            startedAt = LocalDate.of(2025, 1, 10),
            endedAt = LocalDate.of(2025, 1, 10),
            summary = "ok"
        )

        val exception = assertThrows(BadRequestException::class.java) {
            createReportUseCase.execute(request)
        }

        assertEquals("Name should have between 3 and 500 characters", exception.message)

        verify(reportRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when startedAt is before endedAt`() {
        val request = CreateReportRequest(
            userId = 1L,
            startedAt = LocalDate.of(2025, 1, 5),
            endedAt = LocalDate.of(2025, 1, 10),
            summary = "Valid summary"
        )

        val exception = assertThrows(BadRequestException::class.java) {
            createReportUseCase.execute(request)
        }

        assertEquals("Started date must be greater than or equal to ended date", exception.message)

        verify(reportRepository, never()).save(any())
    }
}
