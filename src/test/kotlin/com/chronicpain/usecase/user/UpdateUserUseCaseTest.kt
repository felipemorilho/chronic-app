package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.UpdateUserRequest
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class UpdateUserUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var updateUserUseCase: UpdateUserUseCase

    @Test
    fun `should update user successfully`() {
        val existingUser = User(
         id = 1L,
         name = "Test",
         email = "test@example.com",
         passwordHash = "hash",
         type = "patient"
        )

        val request = UpdateUserRequest(
            name = "Test Atualizado",
            type = "patient",
            userId = 1L
        )

        `when`(userRepository.findById(1L))
            .thenReturn(Optional.of(existingUser))
        `when`(userRepository.save(any()))
            .thenReturn(existingUser.copy(name = request.name!!))

        val response = updateUserUseCase.execute(request)

        assertEquals("Test Atualizado", response.name)
    }

    @Test
    fun `should throw NotFoundException when user does not exist`() {
        val request = UpdateUserRequest(name = "Test", userId = 1L)

        `when`(userRepository.findById(1L))
             .thenReturn(Optional.empty())

        val ex = assertThrows(NotFoundException::class.java) {
         updateUserUseCase.execute(request)
        }

        assertEquals("User with id 1 does not exists", ex.message)
    }

    @Test
    fun `should throw BadRequestException when name is too short`() {
        val existingUser = User(
             id = 1L,
             name = "Felipe",
             email = "test@example.com",
             passwordHash = "hash",
             type = "patient"
        )

        val request = UpdateUserRequest(
            name = "a",
            userId = 1L
        )

        `when`(userRepository.findById(1L))
             .thenReturn(Optional.of(existingUser))

        val ex = assertThrows(BadRequestException::class.java) {
             updateUserUseCase.execute(request)
        }

        assertEquals("Name must be between 3 and 70 characters", ex.message)
    }
}