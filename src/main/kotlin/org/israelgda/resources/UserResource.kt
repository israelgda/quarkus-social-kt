package org.israelgda.resources

import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.dto.UserDTO
import org.israelgda.model.User

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class UserResource {

    @GET
    @Path("{id}")
    fun getById(id: Long): Response {
        val userEntity = User.findById(id)

        return if(userEntity != null)
            Response.ok(entityToDTO(userEntity)).build()
        else
            Response.status(Response.Status.NOT_FOUND).build()
    }

    @GET
    fun getAllUsers(): Response {
        val usersList = User.findAll().list().map {
            entityToDTO(it)
        }
        return Response.ok(usersList).build()
    }

    @POST
    @Transactional
    fun createUser(user: UserDTO): Response {
        val userEntity = dtoToEntity(user)
        userEntity.persist()
        val userDto = entityToDTO(userEntity)

        return Response.ok(userDto).build()
    }

    @PUT
    @Path("{id}")
    @Transactional
    fun updateUser(@PathParam("id") id: Long, userDto: UserDTO): Response {
        val userEntity = User.findById(id)

        if(userEntity != null) {
            val userUpdated = updateEntity(userEntity, userDto)
            userUpdated.persist()
            return Response.ok(userUpdated).build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    private fun updateEntity(userUpdated: User, userDto: UserDTO): User {
        userUpdated.name = userDto.name
        userUpdated.age = userDto.age
        return userUpdated
    }

    @DELETE
    @Path("{id}")
    @Transactional
    fun deleteUser(@PathParam("id") id: Long): Response {
        val userEntity = User.findById(id)

        if(userEntity != null) {
            userEntity.delete()
            return Response.noContent().build()
        } else {
            return Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    private fun entityToDTO(user: User) = UserDTO(user.id, user.name, user.age)

    fun dtoToEntity(user: UserDTO) = User(user.name, user.age)
}