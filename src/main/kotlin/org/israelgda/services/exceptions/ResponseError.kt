package org.israelgda.services.exceptions

import jakarta.validation.ConstraintViolation
import jakarta.ws.rs.core.Response

class ResponseError (
    message: String,
    errors: List<FieldErrorsObject>
) {

    val message: String = message
    val errors: List<FieldErrorsObject> = errors

    fun withStatusCode(code: Int): Response {
        return Response.status(code).entity(this).build()
    }

    companion object{

        const val UNPROCESSABLE_ENTITY_STATUS = 422

        fun <T> createFromValidation(violations: Set<ConstraintViolation<out T>>): ResponseError {
            val message = "Validation Error"
            val errors = violations.map {
                FieldErrorsObject(it.propertyPath.toString(), it.message)
            }

            return ResponseError(message, errors)
        }
    }
}