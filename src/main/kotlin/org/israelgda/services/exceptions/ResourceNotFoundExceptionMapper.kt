package org.israelgda.services.exceptions

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider

@Provider
class ResourceNotFoundExceptionMapper: ExceptionMapper<ResourceNotFoundException> {

    override fun toResponse(exception: ResourceNotFoundException): Response {
        return Response
            .status(Response.Status.NOT_FOUND)
            .entity(ExceptionSimpleMessage(exception.message))
            .build()
    }
}