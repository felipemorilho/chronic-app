package com.chronicpain.config

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
object PostgresContainerConfig {

    @Container
    val postgres = PostgreSQLContainer("postgres:16-alpine").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
        withReuse(true)
        start()
    }
}
