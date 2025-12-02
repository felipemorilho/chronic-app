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
class GetUserUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var getUserUseCase: GetUserUseCase

    @Test
    fun `should return user data when id exists`() {
        val user = User(
            id = 1L,
            name = "Test",
            email = "test@example.com",
            passwordHash = "hash",
            type = "patient"
        )

        `when`(userRepository.findById(1L))
            .thenReturn(Optional.of(user))

        val response = getUserUseCase.execute(1L)

        assertEquals(1L, response.id)
        assertEquals("Test", response.name)
        assertEquals("test@example.com", response.email)
        assertEquals("patient", response.type)

        verify(userRepository).findById(1L)
    }

    @Test
    fun `should throw NotFoundException when user does not exist`() {
        `when`(userRepository.findById(99L))
            .thenReturn(Optional.empty())

        val exception = assertThrows(NotFoundException::class.java) {
            getUserUseCase.execute(99L)
        }

        assertEquals(" User not found for id: 99", exception.message)
        verify(userRepository).findById(99L)
    }
}