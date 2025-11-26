package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.domain.model.User
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
) : UseCase<CreateUserRequest, UserResponse> {

    companion object {
        const val LOG_PREFIX = "[CREATE_USER_USE_CASE"
    }
    private val passwordEncoder = BCryptPasswordEncoder()

    override fun execute(input: CreateUserRequest): UserResponse {
        this.logger().info("{} Starting to create user: {}", LOG_PREFIX, input)

        validateInputEmail(input.email)
        val user = buildUser(input)

        return saveUser(user)
    }

    private fun validateInputEmail(email: String) {
        if (userRepository.existsByEmail(email)) {
            val message = String.format("Already exists user with email: {}", email)
            this.logger().warn("{} {}", LOG_PREFIX, message)
            throw IllegalArgumentException(message)
        }
    }

    private fun buildUser(input: CreateUserRequest): User =
        User(
            name = input.name,
            email = input.email,
            passwordHash = passwordEncoder.encode(input.password),
            type = input.type
        )

    private fun saveUser(user: User): UserResponse {
        val savedUser = userRepository.save(user)
        this.logger().info("{} Successfully saved user: {}", LOG_PREFIX, user)
        return buildUserResponse(savedUser)
    }

    private fun buildUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            type = user.type
        )
    }
}