package com.chronicpain.controller

import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.dto.painregister.PainRegisterResponse
import com.chronicpain.domain.dto.painregister.UpdatePainRegisterRequest
import com.chronicpain.usecase.painregister.*
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
import java.time.LocalDateTime

@WebMvcTest(controllers = [PainRegisterController::class])
@AutoConfigureMockMvc(addFilters = false)
class PainRegisterControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var createPainRegisterUseCase: CreatePainRegisterUseCase
    @MockitoBean
    lateinit var updatePainRegisterUseCase: UpdatePainRegisterUseCase
    @MockitoBean
    lateinit var deletePainRegisterUseCase: DeletePainRegisterUseCase
    @MockitoBean
    lateinit var getUserPainRegisterUseCase: GetUserPainRegisterUseCase
    @MockitoBean
    lateinit var getPainRegisterByIdUseCase: GetPainRegisterByIdUseCase

    @Test
    fun `should create pain register`() {
        val request = CreatePainRegisterRequest(
            userId = 1L,
            intensity = 7,
            description = "Dor forte",
            bodyPart = "Costas",
            occurAt = LocalDateTime.of(2025, 1, 1, 10, 0)
        )

        val response = PainRegisterResponse(
            id = 100L,
            userId = 1L,
            intensity = 7,
            description = "Dor forte",
            bodyPart = "Costas",
            occurAt = request.occurAt!!
        )

        `when`(createPainRegisterUseCase.execute(request))
            .thenReturn(response)

        mockMvc.perform(
            post("/chronicpain/v1/pain")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.intensity").value(7))

        verify(createPainRegisterUseCase).execute(request)
    }

    @Test
    fun `should return pain register by id`() {
        val response = PainRegisterResponse(
            id = 10L,
            userId = 1L,
            intensity = 6,
            description = "Dor média",
            bodyPart = "Nuca",
            occurAt = LocalDateTime.now()
        )

        `when`(getPainRegisterByIdUseCase.execute(10L))
            .thenReturn(response)

        mockMvc.perform(get("/chronicpain/v1/pain/10"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(10L))

        verify(getPainRegisterByIdUseCase).execute(10L)
    }

    @Test
    fun `should return pain registers by user id`() {
        val list = listOf(
            PainRegisterResponse(1L, 1L, 5, "Dor leve", "Pescoço", LocalDateTime.now()),
            PainRegisterResponse(2L, 1L, 8, "Dor forte", "Costas", LocalDateTime.now())
        )

        `when`(getUserPainRegisterUseCase.execute(1L))
            .thenReturn(list)

        mockMvc.perform(get("/chronicpain/v1/pain/user/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))

        verify(getUserPainRegisterUseCase).execute(1L)
    }

    @Test
    fun `should update pain register`() {
        val request = UpdatePainRegisterRequest(
            painRegisterId = 50L,
            intensity = 9,
            description = "Piorou",
            bodyPart = "Lombar",
            occurAt = LocalDateTime.now()
        )

        val response = PainRegisterResponse(
            id = 50L,
            userId = 1L,
            intensity = 9,
            description = "Piorou",
            bodyPart = "Lombar",
            occurAt = request.occurAt!!
        )

        `when`(updatePainRegisterUseCase.execute(request))
            .thenReturn(response)

        mockMvc.perform(
            put("/chronicpain/v1/pain/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.intensity").value(9))

        verify(updatePainRegisterUseCase).execute(request)
    }

    @Test
    fun `should delete pain register`() {
        doNothing().`when`(deletePainRegisterUseCase).execute(5L)

        mockMvc.perform(delete("/chronicpain/v1/pain/5"))
            .andExpect(status().isOk)

        verify(deletePainRegisterUseCase).execute(5L)
    }
}