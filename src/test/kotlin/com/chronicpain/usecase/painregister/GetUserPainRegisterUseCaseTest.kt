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

@ExtendWith(MockitoExtension::class)
class GetUserPainRegisterUseCaseTest {

    @Mock
    lateinit var painRegisterRepository: PainRegisterRepository

    @InjectMocks
    lateinit var getUserPainRegisterUseCase: GetUserPainRegisterUseCase

    private val user = User(
        id = 1L,
        name = "Test User",
        email = "test@example.com",
        passwordHash = "hash",
        type = "patient"
    )

    @Test
    fun `should return pain register list for user`() {
        val painRegisters = listOf(
            PainRegister(
                id = 10L,
                user = user,
                intensity = 7,
                description = "Dor forte",
                bodyPart = "Costas",
                occurAt = LocalDateTime.of(2025, 1, 1, 10, 0)
            ),
            PainRegister(
                id = 11L,
                user = user,
                intensity = 5,
                description = "Dor leve",
                bodyPart = "Pescoço",
                occurAt = LocalDateTime.of(2025, 1, 2, 9, 0)
            )
        )

        `when`(painRegisterRepository.findByUserIdOrderByOccurAtDesc(1L))
            .thenReturn(painRegisters)

        val response = getUserPainRegisterUseCase.execute(1L)

        assertEquals(2, response.size)
        assertEquals(10L, response[0].id)
        assertEquals(11L, response[1].id)
        assertEquals(1L, response[0].userId)

        verify(painRegisterRepository).findByUserIdOrderByOccurAtDesc(1L)
    }

    @Test
    fun `should throw NotFoundException when user has no pain registers`() {
        `when`(painRegisterRepository.findByUserIdOrderByOccurAtDesc(99L))
            .thenReturn(emptyList())

        val exception = assertThrows(NotFoundException::class.java) {
            getUserPainRegisterUseCase.execute(99L)
        }

        assertEquals("No pain register found for user 99", exception.message)

        verify(painRegisterRepository).findByUserIdOrderByOccurAtDesc(99L)
    }
}
