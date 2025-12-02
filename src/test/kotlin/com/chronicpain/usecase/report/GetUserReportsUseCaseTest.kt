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

@ExtendWith(MockitoExtension::class)
class GetUserReportsUseCaseTest {

    @Mock
    lateinit var reportRepository: ReportRepository

    @InjectMocks
    lateinit var getUserReportsUseCase: GetUserReportsUseCase

    private val user = User(
        id = 1L,
        name = "User",
        email = "user@test.com",
        passwordHash = "hash",
        type = "patient"
    )

    private val reports = listOf(
        Report(
            id = 10L,
            user = user,
            startedAt = LocalDate.of(2025, 1, 1),
            endedAt = LocalDate.of(2025, 1, 2),
            summary = "Summary 1"
        ),
        Report(
            id = 11L,
            user = user,
            startedAt = LocalDate.of(2025, 1, 5),
            endedAt = LocalDate.of(2025, 1, 6),
            summary = "Summary 2"
        )
    )

    @Test
    fun `should return list of reports for user`() {
        `when`(reportRepository.findByUserId(1L))
            .thenReturn(reports)

        val response = getUserReportsUseCase.execute(1L)

        assertEquals(2, response.size)
        assertEquals(10L, response[0].id)
        assertEquals(11L, response[1].id)
        assertEquals("Summary 1", response[0].summary)
        assertEquals("Summary 2", response[1].summary)

        verify(reportRepository).findByUserId(1L)
    }

    @Test
    fun `should throw NotFoundException when no reports found for user`() {
        `when`(reportRepository.findByUserId(99L))
            .thenReturn(emptyList())

        val ex = assertThrows(NotFoundException::class.java) {
            getUserReportsUseCase.execute(99L)
        }

        assertEquals("No reports found for user 99", ex.message)

        verify(reportRepository).findByUserId(99L)
    }
}
