package com.chronicpain.controller

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.dto.user.UpdateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.usecase.user.CreateUserUseCase
import com.chronicpain.usecase.user.DeleteUserUseCase
import com.chronicpain.usecase.user.GetAllUsersUseCase
import com.chronicpain.usecase.user.GetUserUseCase
import com.chronicpain.usecase.user.UpdateUserUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = [UserController::class])
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var createUserUseCase: CreateUserUseCase
    @MockitoBean
    lateinit var updateUserUseCase: UpdateUserUseCase
    @MockitoBean
    lateinit var deleteUserUseCase: DeleteUserUseCase
    @MockitoBean
    lateinit var getUserUseCase: GetUserUseCase
    @MockitoBean
    lateinit var getAllUsersUseCase: GetAllUsersUseCase

    @Test
    fun `should create user`() {
        val request = CreateUserRequest(
            name = "Test User",
            email = "test@example.com",
            password = "123456",
            type = "patient"
        )

        val response = UserResponse(
            id = 1L,
            name = "Test User",
            email = "test@example.com",
            type = "patient"
        )

        `when`(createUserUseCase.execute(request))
            .thenReturn(response)

        mockMvc.perform(
            post("/chronicpain/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("Test User"))

        verify(createUserUseCase).execute(request)
    }

    @Test
    fun `should return all users`() {
        val users = listOf(
            UserResponse(1L, "A", "a@example.com", "patient"),
            UserResponse(2L, "B", "b@example.com", "professional")
        )

        `when`(getAllUsersUseCase.execute())
            .thenReturn(users)

        mockMvc.perform(get("/chronicpain/v1/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))

        verify(getAllUsersUseCase).execute()
    }

    @Test
    fun `should return user by id`() {
        val response = UserResponse(1L, "Test", "t@example.com", "patient")

        `when`(getUserUseCase.execute(1L))
            .thenReturn(response)

        mockMvc.perform(get("/chronicpain/v1/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1L))

        verify(getUserUseCase).execute(1L)
    }

    @Test
    fun `should update user`() {
        val input = UpdateUserRequest(
            userId = 1L,
            name = "New",
            email = "new@example.com",
            password = "654321",
            type = "patient"
        )

        val response = UserResponse(1L, "New", "new@example.com", "patient")

        `when`(updateUserUseCase.execute(input))
            .thenReturn(response)

        mockMvc.perform(
            put("/chronicpain/v1/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("New"))

        verify(updateUserUseCase).execute(input)
    }

    @Test
    fun `should delete user`() {
        doNothing().`when`(deleteUserUseCase).execute(1L)

        mockMvc.perform(delete("/chronicpain/v1/users/1"))
            .andExpect(status().isOk)

        verify(deleteUserUseCase).execute(1L)
    }
}
