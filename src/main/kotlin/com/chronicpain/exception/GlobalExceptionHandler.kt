package com.chronicpain.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlerMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ResponseEntity<ApiError> {
        val fieldErros = exception.bindingResult.fieldErrors.associate { error ->
            error.field to (error.defaultMessage ?: "Invalid field")
        }

        val error = ApiError(
            message = "Validation error",
            errors = fieldErros
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handlerConstraintViolationException(exception: ConstraintViolationException): ResponseEntity<ApiError> {
        val fieldErrors = exception.constraintViolations.associate { constraintViolation ->
            val field = constraintViolation.propertyPath.toString().substringAfterLast(".")
            field to constraintViolation.message
        }

        val error = ApiError(
            message = "Validation error",
            errors = fieldErrors
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}