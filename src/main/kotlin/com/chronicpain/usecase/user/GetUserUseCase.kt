package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.domain.model.User
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetUserUseCase(
    private val userRepository: UserRepository
) : UseCase<Long, UserResponse> {

    companion object {
        const val LOG_PREFIX = "[GET_USER_USE_CASE]"
    }

    override fun execute(input: Long): UserResponse {
        this.logger().info("{} Starting to get User for id: {}", LOG_PREFIX, input.toString())

        val user = fetchUserById(input)

        return buildUserResponse(user)
    }

    private fun fetchUserById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow{
                val message = String.format(" User not found for id: {}", userId.toString())
                this.logger().error("{} {}", LOG_PREFIX, message)
                NoSuchElementException(message)
            }
    }

    private fun buildUserResponse(user: User): UserResponse =
        UserResponse(
            id = user.id,
            name = user.name,
            email = user.email,
            type = user.type
        )
}