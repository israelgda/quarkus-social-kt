package org.israelgda.resources

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.services.PostService

@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class PostFindDeleteResource {

    @Inject
    lateinit var postService: PostService

    @GET
    @Path("{postId}")
    fun getById(@PathParam("postId") postId: Long): Response {
        val post = postService.findById(postId)
        return Response.ok().entity(post).build()
    }

    @DELETE
    @Path("{postId}")
    @Transactional
    fun delete(@PathParam("postId") postId: Long): Response {
        postService.deleteById(postId)
        return Response.status(201).build()
    }

}