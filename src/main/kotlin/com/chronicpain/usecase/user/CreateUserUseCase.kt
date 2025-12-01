package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.CreateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.ConflictException
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
        const val LOG_PREFIX = "[CREATE_USER_USE_CASE]"
    }

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun execute(input: CreateUserRequest): UserResponse {
        this.logger().info("$LOG_PREFIX Starting to create user: $input")

        validateInput(input)
        val user = buildUser(input)

        return saveUser(user)
    }

    private fun validateInput(input: CreateUserRequest) {
        validateInputName(input.name)
        validateInputEmail(input.email)
        validateInputPassword(input.password)
        validateInputType(input.type)
    }

    private fun validateInputName(inputName: String) {
        if (inputName.isEmpty()) throw BadRequestException("Name is required")

        if (inputName.length < 3 || inputName.length > 70)  throw BadRequestException("Name should have between 3 and 70 characters")
    }

    private fun validateInputEmail(email: String) {
        if (email.isEmpty()) throw BadRequestException("Email is required")

        if (userRepository.existsByEmail(email)) {
            val message = String.format("Already exists user with email: $email")
            this.logger().warn("$LOG_PREFIX $message")
            throw ConflictException(message)
        }
    }

    private fun validateInputPassword(password: String) {
        if (password.isEmpty()) throw BadRequestException("Password is required")

        if (password.length < 6 || password.length > 15) throw BadRequestException("Password should have between 6 and 15 characters")

    }

    private fun validateInputType(type: String) {
        if (type.isEmpty()) throw BadRequestException("Type is required")

        if (type.length < 2 || type.length > 20) throw BadRequestException("Type should have between 2 and 20 characters.")

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
        this.logger().info("$LOG_PREFIX Successfully saved user: ${user.id}")
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