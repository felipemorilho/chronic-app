package com.chronicpain.config

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext

class TestContainerInitializer :
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        val container = PostgresContainerConfig.postgres

        TestPropertyValues.of(
            "spring.datasource.url=${container.jdbcUrl}",
            "spring.datasource.username=${container.username}",
            "spring.datasource.password=${container.password}",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        ).applyTo(context.environment)
    }
}
