package com.chronicpain.usecase.painregister

import com.chronicpain.domain.model.PainRegister
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.PainRegisterRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DeletePainRegisterUseCaseTest {

    @Mock
    lateinit var painRegisterRepository: PainRegisterRepository

    @InjectMocks
    lateinit var deletePainRegisterUseCase: DeletePainRegisterUseCase

    @Test
    fun `should delete pain register successfully`() {
        val painRegister = PainRegister(
            id = 10L,
            user = mock(),
            intensity = 5,
            description = "desc",
            bodyPart = "costas",
            occurAt = java.time.LocalDateTime.now()
        )

        `when`(painRegisterRepository.findById(10L))
            .thenReturn(Optional.of(painRegister))

        deletePainRegisterUseCase.execute(10L)

        verify(painRegisterRepository).findById(10L)
        verify(painRegisterRepository).delete(painRegister)
    }

    @Test
    fun `should throw NotFoundException when pain register does not exist`() {
        `when`(painRegisterRepository.findById(999L)).thenReturn(Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            deletePainRegisterUseCase.execute(999L)
        }

        assertEquals("Pain register 999 not found", exception.message)

        verify(painRegisterRepository).findById(999L)
        verify(painRegisterRepository, never()).delete(any())
    }
}
