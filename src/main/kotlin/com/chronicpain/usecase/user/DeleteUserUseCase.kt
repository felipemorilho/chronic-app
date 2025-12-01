package com.chronicpain.usecase.user

import com.chronicpain.domain.model.User
import com.chronicpain.exception.NotFoundException
import com.chronicpain.repository.UserRepository
import com.chronicpain.usecase.UseCase
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class DeleteUserUseCase(
    private val userRepository: UserRepository,
) : UseCase<Long, Unit> {

    companion object {
        const val LOG_PREFIX = "[DELETE_USER_USE_CASE]"
    }

    override fun execute(input: Long) {
        this.logger().info("$LOG_PREFIX Starting to delete user with id $input")

        val existingUser = fetchUser(input)

        userRepository.delete(existingUser)

        this.logger().info("$LOG_PREFIX Successfully deleted user with id $input")
    }

    private fun fetchUser(input: Long): User =
        userRepository.findById(input)
            .orElseThrow {
                val message = "User $input not found"
                this.logger().warn("$LOG_PREFIX $message")
                throw NotFoundException(message)
            }
}