package com.chronicpain.controller

import com.chronicpain.config.TestContainerInitializer
import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.model.User
import com.chronicpain.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = [TestContainerInitializer::class])
class PainRegisterControllerIT(

    @Autowired val mockMvc: MockMvc,
    @Autowired val userRepository: UserRepository,
    @Autowired val mapper: ObjectMapper

) {

    @Test
    fun `should create pain register end-to-end`() {
        val user = userRepository.save(
            User(0,"Felipe Test", "felipe@testexample.com", "1235321", "patient")
        )

        val request = CreatePainRegisterRequest(
            userId = user.id,
            intensity = 7,
            description = "Headache",
            bodyPart = "Head",
            occurAt = null
        )

        mockMvc.post("/chronicpain/v1/pain") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
        }
    }
}