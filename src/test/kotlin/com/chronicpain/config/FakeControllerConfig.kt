package com.chronicpain.config

import com.chronicpain.exception.BadRequestException
import com.chronicpain.exception.ConflictException
import com.chronicpain.exception.GlobalExceptionHandlerTest.FakeRequest
import com.chronicpain.exception.NotFoundException
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class FakeControllerConfig {

    @GetMapping("/notfound")
    fun throwNotFound(): String {
        throw NotFoundException("Not found test")
    }

    @GetMapping("/conflict")
    fun throwConflict(): String {
        throw ConflictException("Conflict test")
    }

    @GetMapping("/badrequest")
    fun throwBadRequest(): String {
        throw BadRequestException("Bad request test")
    }

    @GetMapping("/generic")
    fun throwGeneric(): String {
        throw RuntimeException("Unexpected failure")
    }

    @PostMapping("/validation")
    fun validation(@Valid @RequestBody body: FakeRequest): String = "ok"

    @GetMapping("/constraint")
    fun constraint(
        @Min(5, message = "Value must be >= 5")
        @RequestParam value: Int
    ): String = "ok"
}