package org.israelgda.resources

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.dto.UserDTO
import org.israelgda.services.UserService

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserResource {

    @Inject
    lateinit var userService: UserService

    @GET
    @Path("{id}")
    fun getById(@PathParam("id") id: Long): Response {
        val userEntity = userService.findById(id)

        return Response.ok(userEntity).build()
    }

    @GET
    fun getAll(): Response {
        val usersList = userService.getAll()

        return Response.ok(usersList).build()
    }

    @POST
    @Transactional
    fun create(userDto: UserDTO): Response {
        val userCreated = userService.create(userDto)

        return Response.ok(userCreated).build()
    }

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, userDto: UserDTO): Response {
        val userUpdated = userService.update(id, userDto)

        return Response.ok(userUpdated).build()
    }

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        userService.delete(id)
        return Response.noContent().build()
    }
}