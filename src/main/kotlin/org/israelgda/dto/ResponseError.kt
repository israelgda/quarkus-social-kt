package org.israelgda.dto

import jakarta.validation.ConstraintViolation
import jakarta.ws.rs.core.Response

class ResponseError (
    message: String,
    errors: List<FieldErrorsDto>
) {

    val message: String = message
    val errors: List<FieldErrorsDto> = errors

    fun withStatusCode(code: Int): Response {
        return Response.status(code).entity(this).build()
    }

    companion object{

        const val UNPROCESSABLE_ENTITY_STATUS = 422

        fun <T> createFromValidation(violations: Set<ConstraintViolation<out T>>): ResponseError {
            val message = "Validation Error"
            val errors = violations.map {
                FieldErrorsDto(it.propertyPath.toString(), it.message)
            }

            return ResponseError(message, errors)
        }
    }
}