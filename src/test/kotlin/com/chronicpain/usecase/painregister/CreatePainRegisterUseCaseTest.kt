package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.CreatePainRegisterRequest
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class CreatePainRegisterUseCaseTest {

    @Mock
    lateinit var painRegisterRepository: PainRegisterRepository

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var createPainRegisterUseCase: CreatePainRegisterUseCase

    @Test
    fun `should create pain register successfully`() {
        // Arrange
        val request = CreatePainRegisterRequest(
            userId = 1L,
            intensity = 8,
            description = "Dor forte",
            bodyPart = "Lombar",
            occurAt = LocalDateTime.of(2025, 1, 1, 10, 0)
        )

        val user = User(
            id = 1L,
            name = "Test User",
            email = "test@example.com",
            passwordHash = "hash",
            type = "patient"
        )

        val savedPainRegister = request.occurAt?.let {
            PainRegister(
                id = 10L,
                user = user,
                intensity = request.intensity,
                description = request.description,
                bodyPart = request.bodyPart,
                occurAt = it
            )
        }

        `when`(userRepository.findById(any()))
            .thenReturn(Optional.of(user))
        `when`(painRegisterRepository.save(any()))
            .thenReturn(savedPainRegister)

        // Act
        val response = createPainRegisterUseCase.execute(request)

        // Assert
        assertEquals(10L, response.id)
        assertEquals(1L, response.userId)
        assertEquals(8, response.intensity)
        assertEquals("Dor forte", response.description)
        assertEquals("Lombar", response.bodyPart)
        assertEquals(request.occurAt, response.occurAt)

        verify(userRepository).findById(any())
        verify(painRegisterRepository).save(any())
    }

    @Test
    fun `should throw NotFoundException when user does not exist`() {
        // Arrange
        val request = CreatePainRegisterRequest(
            userId = 99L,
            intensity = 5,
            description = "Teste",
            bodyPart = "Cabeça",
            occurAt = null
        )

        `when`(userRepository.findById(99L))
            .thenReturn(Optional.empty())

        // Act
        val exception = assertThrows(NotFoundException::class.java) {
            createPainRegisterUseCase.execute(request)
        }

        // Assert
        assertEquals("User 99 not found", exception.message)
        verify(userRepository).findById(99L)
    }

    @Test
    fun `should throw BadRequestException when intensity is invalid`() {
        val request = CreatePainRegisterRequest(
            userId = 1L,
            intensity = 20,
            description = "Muito forte",
            bodyPart = "Costas",
            occurAt = null
        )

        val exception = assertThrows(BadRequestException::class.java) {
            createPainRegisterUseCase.execute(request)
        }

        assertEquals("Intensity must be between 1 and 10", exception.message)
        verify(userRepository, never()).findById(anyLong())
        verify(painRegisterRepository, never()).save(any())
    }
}
