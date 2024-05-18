package org.israelgda.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.israelgda.dto.FollowerRequest
import org.israelgda.dto.FollowersResponse
import org.israelgda.dto.UserFollowerResponse
import org.israelgda.model.Follower
import org.israelgda.repositories.FollowerRepository
import org.israelgda.repositories.UserRepository

@ApplicationScoped
class FollowerService {

    @Inject
    lateinit var followerRepository: FollowerRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Transactional
    fun followByIds(userId: Long, toFollow: FollowerRequest) {
        val followerId = toFollow.followerId

        val user = userRepository.findById(userId)
            ?: throw RuntimeException("Entity User not found for id: $userId")

        val userToFollow = userRepository.findById(followerId)
            ?: throw RuntimeException("Entity User not found for id: $followerId")

        if (!followerRepository.alreadyFollows(userToFollow, user)) {
            val follower = Follower(user, userToFollow)
            followerRepository.persist(follower)
        }

    }

    fun getAllByUserId(userId: Long): FollowersResponse {
        val followersResponseList = userRepository.findById(userId)
            ?.let { followerRepository.findByUserId(userId) }
            ?: throw RuntimeException("Entity User not found for id: $userId")

        val followersDtoList = followersResponseList.map { follower ->
            follower.let { user ->
                userRepository.findById(user.follower.id)
                    ?.let {
                        UserFollowerResponse(it.id, it.name)
                    }!!
            }
        }

        return FollowersResponse(followersResponseList.size, followersDtoList)
    }

}
