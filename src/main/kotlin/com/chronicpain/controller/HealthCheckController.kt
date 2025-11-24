package com.chronicpain.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/v1/healthcheck"])
class HealthCheckController {

    @GetMapping
    fun health(): Map<String, String> {
        return mapOf("status" to "UP")
    }
}