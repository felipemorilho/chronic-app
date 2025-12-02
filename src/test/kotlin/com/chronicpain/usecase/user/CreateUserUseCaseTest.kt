package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.ConflictException
import com.chronicpain.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@ExtendWith(MockitoExtension::class)
class CreateUserUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var createUserUseCase: CreateUserUseCase

    @Test
    fun `should create user successfully`() {
     val request = CreateUserRequest(
      name = "Test",
      email = "test@example.com",
      password = "123456",
      type = "patient"
     )

     val savedUser = User(
      id = 1L,
      name = request.name,
      email = request.email,
      passwordHash = BCryptPasswordEncoder().encode(request.password),
      type = request.type
     )

     `when`(userRepository.existsByEmail(request.email))
         .thenReturn(false)
     `when`(userRepository.save(any()))
         .thenReturn(savedUser)

     val response = createUserUseCase.execute(request)

     assertEquals(1L, response.id)
     assertEquals("Test", response.name)
     assertEquals("test@example.com", response.email)
     assertEquals("patient", response.type)

     verify(userRepository).existsByEmail(request.email)
     verify(userRepository).save(any(User::class.java))
    }

    @Test
    fun `should throw ConflictException when email already exists`() {
        val request = CreateUserRequest(
            name = "Test",
            email = "test@example.com",
            password = "123456",
            type = "patient"
        )

        `when`(userRepository.existsByEmail(request.email)).thenReturn(true)

        val exception = assertThrows(ConflictException::class.java) {
            createUserUseCase.execute(request)
        }

        assertEquals("Already exists user with email: test@example.com", exception.message)
    }

    @Test
    fun `should throw BadRequestException when password is too short`() {
        val request = CreateUserRequest(
            name = "Test",
            email = "test@example.com",
            password = "1",
            type = "patient"
        )

        val exception = assertThrows(BadRequestException::class.java) {
            createUserUseCase.execute(request)
        }

        assertEquals("Password should have between 6 and 15 characters", exception.message)
    }
}