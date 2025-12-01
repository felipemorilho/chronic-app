package com.chronicpain.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<ApiError> {
        val error = ApiError(message = exception.message ?: "Not found")
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(exception: ConflictException): ResponseEntity<ApiError> {
        val error = ApiError(message = exception.message ?: "Conflict error on request")
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequest(exception: BadRequestException): ResponseEntity<ApiError> {
        val error = ApiError(message = exception.message ?: "Bad request")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(exception: Exception): ResponseEntity<ApiError> {
        val error = ApiError(message = "Unexpected error on request", details = exception.message)
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}