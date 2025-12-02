package com.chronicpain.repository

import com.chronicpain.config.TestContainerInitializer
import com.chronicpain.domain.model.PainRegister
import com.chronicpain.domain.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@SpringBootTest
@ContextConfiguration(initializers = [TestContainerInitializer::class])
class PainRegisterRepositoryIT(

    @Autowired val painRegisterRepository: PainRegisterRepository,
    @Autowired val userRepository: UserRepository

) {

    @Test
    fun `should save and retrieve pain registers by user`() {
        val user = userRepository.save(
            User(
                name = "Ana",
                email = "ana@test.com",
                passwordHash = "123",
                type = "patient"
            )
        )

        val register = painRegisterRepository.save(
            PainRegister(
                user = user,
                intensity = 8,
                description = "Strong pain",
                bodyPart = "Back",
                occurAt = LocalDateTime.now()
            )
        )

        val list = painRegisterRepository.findByUserIdOrderByOccurAtDesc(user.id!!)

        assertEquals(1, list.size)
        assertEquals(register.id, list[0].id)
    }
}
