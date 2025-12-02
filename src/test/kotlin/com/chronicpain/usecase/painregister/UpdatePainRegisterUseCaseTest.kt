package com.chronicpain.usecase.painregister

import com.chronicpain.domain.dto.painregister.UpdatePainRegisterRequest
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
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
class UpdatePainRegisterUseCaseTest {

    @Mock
    lateinit var painRegisterRepository: PainRegisterRepository

    @InjectMocks
    lateinit var updatePainRegisterUseCase: UpdatePainRegisterUseCase

    private val user = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    private val existingPainRegister = PainRegister(
        id = 10L,
        user = user,
        intensity = 5,
        description = "dor moderada",
        bodyPart = "costas",
        occurAt = LocalDateTime.of(2025, 1, 1, 10, 0)
    )

    @Test
    fun `should update pain register successfully`() {
        val request = UpdatePainRegisterRequest(
            painRegisterId = 10L,
            intensity = 8,
            description = "Nova descrição",
            bodyPart = "Lombar",
            occurAt = LocalDateTime.of(2025, 1, 2, 12, 0)
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(existingPainRegister))
        `when`(painRegisterRepository.save(any(PainRegister::class.java)))
            .thenReturn(request.occurAt?.let {
                existingPainRegister.copy(
                    intensity = request.intensity!!,
                    description = request.description,
                    bodyPart = request.bodyPart,
                    occurAt = it
                )
            })

        val response = updatePainRegisterUseCase.execute(request)

        assertEquals(10L, response.id)
        assertEquals(1L, response.userId)
        assertEquals(8, response.intensity)
        assertEquals("Nova descrição", response.description)
        assertEquals("Lombar", response.bodyPart)
        assertEquals(request.occurAt, response.occurAt)

        verify(painRegisterRepository).findById(10L)
        verify(painRegisterRepository).save(any(PainRegister::class.java))
    }

    @Test
    fun `should throw NotFoundException when pain register does not exist`() {
        val request = UpdatePainRegisterRequest(
            painRegisterId = 999L,
            intensity = 5
        )

        `when`(painRegisterRepository.findById(999L))
            .thenReturn(Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            updatePainRegisterUseCase.execute(request)
        }

        assertEquals("Pain Register 999 not found", exception.message)

        verify(painRegisterRepository).findById(999L)
        verify(painRegisterRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when no fields are provided`() {
        val request = UpdatePainRegisterRequest(
            painRegisterId = 10L
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(existingPainRegister))

        val exception = assertThrows(BadRequestException::class.java) {
            updatePainRegisterUseCase.execute(request)
        }

        assertEquals("At least one field must be provided to update the pain register", exception.message)

        verify(painRegisterRepository).findById(10L)
        verify(painRegisterRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException for invalid intensity`() {
        val request = UpdatePainRegisterRequest(
            painRegisterId = 10L,
            intensity = 50
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(existingPainRegister))

        val exception = assertThrows(BadRequestException::class.java) {
            updatePainRegisterUseCase.execute(request)
        }

        assertEquals("Intensity must be between 1 and 10", exception.message)
        verify(painRegisterRepository).findById(10L)
        verify(painRegisterRepository, never()).save(any())
    }

    @Test
    fun `should throw BadRequestException when occurAt is in the future`() {
        val futureDate = LocalDateTime.now().plusDays(1)

        val request = UpdatePainRegisterRequest(
            painRegisterId = 10L,
            occurAt = futureDate
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(existingPainRegister))

        val exception = assertThrows(BadRequestException::class.java) {
            updatePainRegisterUseCase.execute(request)
        }

        assertEquals("Occur date cannot be in the future", exception.message)

        verify(painRegisterRepository).findById(10L)
        verify(painRegisterRepository, never()).save(any())
    }
}
