package org.israelgda.resources

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.dto.PostDTO
import org.israelgda.repositories.FollowerRepository
import org.israelgda.services.FollowerService
import org.israelgda.services.PostService
import org.israelgda.services.UserService

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class PostListCreateResource {

    @Inject
    lateinit var postService: PostService

    @Inject
    lateinit var followerService: FollowerService

    @Inject
    lateinit var userService: UserService

    @GET
    fun getAllByUserId(@PathParam("userId") userId: Long, @HeaderParam("followerId") followerId: Long?): Response {
        if(followerId != null){
            if(followerService.alreadyFollows(followerId, userId)){
                val postList = postService.getAllByUserId(userId)
                return Response.ok().entity(postList).build()
            } else {
                return Response
                    .status(Response.Status.FORBIDDEN)
                    .entity("É necessário seguir o usuário para ver seus posts")
                    .build()
            }
        } else {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("É necessário informar o Header 'followerId'.")
                .build()
        }

    }

    @POST
    @Transactional
    fun create(@PathParam("userId") userId: Long, postDTO: PostDTO): Response {
        val postCreated = postService.create(userId, postDTO)

        return Response.status(201).entity(postCreated).build()
    }

}