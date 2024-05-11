package org.israelgda.resources

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.dto.PostDTO
import org.israelgda.services.PostService

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class PostResource {

    @Inject
    lateinit var postService: PostService

    @GET
    fun getAllByUserId(@PathParam("userId") userId: Long): Response {
        val postList = postService.getAllByUserId(userId)

        return Response.ok().entity(postList).build()
    }

    @POST
    @Transactional
    fun create(@PathParam("userId") userId: Long, postDTO: PostDTO): Response {
        val postCreated = postService.create(userId, postDTO)

        return Response.status(201).build()
    }
}