package com.chronicpain.usecase.user

import com.chronicpain.domain.model.User
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class DeleteUserUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var deleteUserUseCase: DeleteUserUseCase

    @Test
    fun `should delete user successfully`() {
        val existingUser = User(
            id = 1L,
            name = "Test",
            email = "test@example.com",
            passwordHash = "hash",
            type = "patient"
        )

        `when`(userRepository.findById(1L))
            .thenReturn(Optional.of(existingUser))

        deleteUserUseCase.execute(1L)

        verify(userRepository).findById(1L)
        verify(userRepository).delete(existingUser)
    }

    @Test
    fun `should throw NotFoundException when user does not exist`() {
        `when`(userRepository.findById(999L))
            .thenReturn(Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            deleteUserUseCase.execute(999L)
        }

        assertEquals("User 999 not found", exception.message)

        verify(userRepository).findById(999L)
    }
}
