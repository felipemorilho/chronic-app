package com.chronicpain.controller

import com.chronicpain.domain.dto.report.CreateReportRequest
import com.chronicpain.domain.dto.report.ReportResponse
import com.chronicpain.domain.dto.report.UpdateReportRequest
import com.chronicpain.usecase.report.*
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate
import java.time.LocalDateTime

@WebMvcTest(controllers = [ReportController::class])
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var createReportUseCase: CreateReportUseCase
    @MockitoBean
    lateinit var updateReportUseCase: UpdateReportUseCase
    @MockitoBean
    lateinit var deleteReportUseCase: DeleteReportUseCase
    @MockitoBean
    lateinit var getUserReportsUseCase: GetUserReportsUseCase
    @MockitoBean
    lateinit var getReportByIdUseCase: GetReportByIdUseCase

    // -------------------------------------------------------
    // CREATE
    // -------------------------------------------------------
    @Test
    fun `should create report`() {
        val request = CreateReportRequest(
            userId = 1L,
            startedAt = LocalDate.of(2025, 1, 1),
            endedAt = LocalDate.of(2025, 1, 2),
            summary = "Report summary"
        )

        val response = ReportResponse(
            id = 100L,
            userId = 1L,
            startedAt = request.startedAt,
            endedAt = request.endedAt,
            summary = request.summary,
            generatedAt = LocalDateTime.now()
        )

        `when`(createReportUseCase.execute(request)).thenReturn(response)

        mockMvc.perform(
            post("/chronicpain/v1/report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.summary").value("Report summary"))

        verify(createReportUseCase).execute(request)
    }

    // -------------------------------------------------------
    // GET BY ID
    // -------------------------------------------------------
    @Test
    fun `should get report by id`() {
        val response = ReportResponse(
            id = 10L,
            userId = 1L,
            startedAt = LocalDate.of(2025, 1, 1),
            endedAt = LocalDate.of(2025, 1, 2),
            summary = "Test summary",
            generatedAt = LocalDateTime.now()
        )

        `when`(getReportByIdUseCase.execute(10L)).thenReturn(response)

        mockMvc.perform(get("/chronicpain/v1/report/10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(10L))

        verify(getReportByIdUseCase).execute(10L)
    }

    // -------------------------------------------------------
    // GET REPORTS BY USER ID
    // -------------------------------------------------------
    @Test
    fun `should get reports by user id`() {
        val list = listOf(
            ReportResponse(
                id = 1L,
                userId = 1L,
                startedAt = LocalDate.of(2025, 2, 1),
                endedAt = LocalDate.of(2025, 2, 2),
                summary = "A",
                generatedAt = LocalDateTime.now()
            ),
            ReportResponse(
                id = 2L,
                userId = 1L,
                startedAt = LocalDate.of(2025, 3, 1),
                endedAt = LocalDate.of(2025, 3, 2),
                summary = "B",
                generatedAt = LocalDateTime.now()
            )
        )

        `when`(getUserReportsUseCase.execute(1L)).thenReturn(list)

        mockMvc.perform(get("/chronicpain/v1/report/user/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))

        verify(getUserReportsUseCase).execute(1L)
    }

    // -------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------
    @Test
    fun `should update report`() {
        val request = UpdateReportRequest(
            reportId = 50L,
            startedAt = LocalDate.of(2025, 5, 1),
            endedAt = LocalDate.of(2025, 5, 2),
            summary = "Updated summary"
        )

        val response = ReportResponse(
            id = 50L,
            userId = 1L,
            startedAt = request.startedAt!!,
            endedAt = request.endedAt,
            summary = request.summary!!,
            generatedAt = LocalDateTime.now()
        )

        `when`(updateReportUseCase.execute(request)).thenReturn(response)

        mockMvc.perform(
            put("/chronicpain/v1/report/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.summary").value("Updated summary"))

        verify(updateReportUseCase).execute(request)
    }

    // -------------------------------------------------------
    // DELETE
    // -------------------------------------------------------
    @Test
    fun `should delete report`() {
        doNothing().`when`(deleteReportUseCase).execute(5L)

        mockMvc.perform(delete("/chronicpain/v1/report/5"))
            .andExpect(status().isOk)

        verify(deleteReportUseCase).execute(5L)
    }
}

