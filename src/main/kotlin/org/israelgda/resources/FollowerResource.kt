package org.israelgda.resources

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.israelgda.dto.FollowerRequest
import org.israelgda.services.FollowerService

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class FollowerResource {

    @Inject
    lateinit var followerService: FollowerService

    @GET
    fun getAllByUserId(@PathParam("userId") userId: Long): Response {
        val followers = followerService.getAllByUserId(userId)
        return Response.ok().entity(followers).build()
    }

    @PUT
    @Transactional
    fun follow(@PathParam("userId") userId: Long, toFollow: FollowerRequest): Response {
        followerService.followByIds(userId, toFollow)

        return if (userId == toFollow.followerId)
            Response.status(Response.Status.CONFLICT).build()
        else
            Response.noContent().build()
    }

    @DELETE
    @Transactional
    fun unfollow(@PathParam("userId") userId: Long, @QueryParam("unfollow") toUnfollow: Long): Response {
        followerService.unfollowByUsersIds(userId, toUnfollow)
        return Response.noContent().build()
    }
}