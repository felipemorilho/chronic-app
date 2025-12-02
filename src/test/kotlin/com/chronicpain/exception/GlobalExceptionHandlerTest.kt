package com.chronicpain.exception

import com.chronicpain.config.FakeControllerConfig
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.bind.annotation.*
import com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(controllers = [FakeControllerConfig::class])
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class)
class GlobalExceptionHandlerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    data class FakeRequest(
        @field:jakarta.validation.constraints.NotBlank(message = "Name is required")
        val name: String?
    )

    @Test
    fun `should handle NotFoundException`() {
        mockMvc.perform(get("/test/notfound"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.message").value("Not found test"))
    }

    @Test
    fun `should handle ConflictException`() {
        mockMvc.perform(get("/test/conflict"))
            .andExpect(status().isConflict)
            .andExpect(jsonPath("$.message").value("Conflict test"))
    }

    @Test
    fun `should handle BadRequestException`() {
        mockMvc.perform(get("/test/badrequest"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Bad request test"))
    }

    @Test
    fun `should handle Generic Exception`() {
        mockMvc.perform(get("/test/generic"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.message").value("Unexpected error on request"))
            .andExpect(jsonPath("$.details").value("Unexpected failure"))
    }

    @Test
    fun `should handle MethodArgumentNotValidException`() {
        val invalidRequest = mapOf("name" to "")

        mockMvc.perform(
            post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Validation error"))
            .andExpect(jsonPath("$.errors.name").value("Name is required"))
    }

    @Test
    fun `should handle ConstraintViolationException`() {
        mockMvc.perform(get("/test/constraint?value=1"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Validation error"))
            .andExpect(jsonPath("$.errors.value").value("Value must be >= 5"))
    }
}
