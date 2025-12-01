package com.chronicpain.usecase.user

import com.chronicpain.domain.dto.user.UserResponse
import com.chronicpain.repository.UserRepository
import com.chronicpain.utils.logger
import org.springframework.stereotype.Service

@Service
class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {

    companion object {
        const val LOG_PREFIX = "[GET_ALL_USERS_USE_CASE]"
    }

    fun execute(): List<UserResponse> {
        this.logger().info("$LOG_PREFIX Starting to fetch all users")

        val users = userRepository.findAll().toList()

        this.logger().info("$LOG_PREFIX Successfully fetched ${users.size} users")

        return users.map {
            UserResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                type = it.type
            )
        }
    }
}