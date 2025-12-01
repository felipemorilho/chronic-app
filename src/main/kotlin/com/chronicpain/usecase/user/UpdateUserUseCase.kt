package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.UpdateUserRequest
import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.domain.model.User
import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.ConflictException
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository
) : UseCase<UpdateUserRequest, UserResponse> {

    companion object {
        const val LOG_PREFIX = "[UPDATE_USER_USE_CASE]"
    }

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun execute(input: UpdateUserRequest): UserResponse {
        this.logger().info("$LOG_PREFIX Starting to update user id=${input.userId}")

        val existingUser = validateUser(input.userId)
        validateInput(input, existingUser)

        val updatedUser = updateFields(existingUser, input)
        return saveUser(updatedUser)
    }

    private fun validateUser(id: Long): User =
        userRepository.findById(id)
            .orElseThrow {
                val message = String.format("User with $id does not exists")
                this.logger().warn("$LOG_PREFIX $message")
                NotFoundException(message)
        }

    private fun validateInput(request: UpdateUserRequest, existingUser: User) {
        request.name?.let { validateName(it) }
        request.email?.let { validateEmail(it, existingUser) }
        request.password?.let { validatePassword(it) }
        request.type?.let { validateType(it) }

        if (
            request.name == null &&
            request.email == null &&
            request.password == null &&
            request.type == null
        ) throw BadRequestException("At least one field must be provided to update the user")

    }

    private fun validateName(name: String) {
        if (name.length < 3 || name.length > 70)
            throw BadRequestException("Name must be between 3 and 70 characters")
    }

    private fun validateEmail(newEmail: String, existingUser: User) {
        if (newEmail != existingUser.email && userRepository.existsByEmail(newEmail)) {
            val message = "Already exists user with email: $newEmail"
            this.logger().warn("$LOG_PREFIX $message")
            throw ConflictException(message)
        }
    }

    private fun validatePassword(password: String) {
        if (password.length < 6 || password.length > 15)
            throw BadRequestException("Password must be between 6 and 15 characters")
    }

    private fun validateType(type: String) {
        if (type.length < 2 || type.length > 20)
            throw BadRequestException("Type must be between 2 and 20 characters")
    }

    private fun updateFields(user: User, request: UpdateUserRequest): User =
        user.copy(
            name = request.name ?: user.name,
            email = request.email ?: user.email,
            passwordHash = request.password?.let { passwordEncoder.encode(it) } ?: user.passwordHash,
            type = request.type ?: user.type
        )

    private fun saveUser(user: User): UserResponse {
        val saved = userRepository.save(user)
        this.logger().info("$LOG_PREFIX Successfully updated user: id=${saved.id}")
        return buildUserResponse(saved)
    }

    private fun buildUserResponse(user: User): UserResponse =
        UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            type = user.type
        )
}