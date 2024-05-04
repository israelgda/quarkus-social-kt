package org.israelgda.dto

import jakarta.validation.ConstraintViolation

class ResponseError (
    message: String,
    errors: List<FieldErrorsDto>
) {

    val message: String = message
    val errors: List<FieldErrorsDto> = errors

    companion object{

        fun <T> createFromValidation(violations: Set<ConstraintViolation<out T>>): ResponseError {
            val message = "Validation Error"
            val errors = violations.map {
                FieldErrorsDto(it.propertyPath.toString(), it.message)
            }

            return ResponseError(message, errors)
        }
    }
}