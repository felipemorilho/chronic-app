package com.chronicpain.usecase.painregister

import com.chronicpain.domain.model.PainRegister
import com.chronicpain.domain.model.User
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
class GetPainRegisterByIdUseCaseTest {

    @Mock
    lateinit var painRegisterRepository: PainRegisterRepository

    @InjectMocks
    lateinit var getPainRegisterByIdUseCase: GetPainRegisterByIdUseCase

    @Test
    fun `should return pain register when id exists`() {
        val user = User(
            id = 1L,
            name = "Test",
            email = "test@example.com",
            passwordHash = "hash",
            type = "patient"
        )

        val painRegister = PainRegister(
            id = 10L,
            user = user,
            intensity = 7,
            description = "Dor forte",
            bodyPart = "Costas",
            occurAt = LocalDateTime.of(2025, 1, 1, 10, 0)
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(painRegister))

        val response = getPainRegisterByIdUseCase.execute(10L)

        assertEquals(10L, response.id)
        assertEquals(1L, response.userId)
        assertEquals(7, response.intensity)
        assertEquals("Dor forte", response.description)
        assertEquals("Costas", response.bodyPart)
        assertEquals(painRegister.occurAt, response.occurAt)

        verify(painRegisterRepository).findById(10L)
    }

    @Test
    fun `should throw NotFoundException when pain register does not exist`() {
        `when`(painRegisterRepository.findById(999L))
            .thenReturn(Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            getPainRegisterByIdUseCase.execute(999L)
        }

        assertEquals("Pain Register 999 not found", exception.message)

        verify(painRegisterRepository).findById(999L)
    }
}