package com.chronicpain.usecase.user

import com.chronicpain.domain.model.User
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GetAllUsersUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase

    @Test
    fun `should return list of users`() {
        val users = listOf(
            User(1L, "Test", "test@example.com", "hash", "patient"),
            User(2L, "Ana", "ana@example.com", "hash", "professional")
        )

        `when`(userRepository.findAll())
            .thenReturn(users)

        val response = getAllUsersUseCase.execute()

        assertEquals(2, response.size)
        assertEquals("Test", response[0].name)
        assertEquals("Ana", response[1].name)

        verify(userRepository).findAll()
    }
}