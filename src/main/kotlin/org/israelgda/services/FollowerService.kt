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
import org.israelgda.services.exceptions.ResourceNotFoundException

@ApplicationScoped
class FollowerService {

    @Inject
    lateinit var followerRepository: FollowerRepository

    @Inject
    lateinit var userRepository: UserRepository

    @Transactional
    fun followByIds(userId: Long, toFollow: FollowerRequest) {
        val followerId = toFollow.followerId

        val userToFollow = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("Entity User not found for id: $userId")

        val userFollower = userRepository.findById(followerId)
            ?: throw ResourceNotFoundException("Entity User not found for id: $followerId")

        if (!followerRepository.alreadyFollows(userFollower, userToFollow)) {
            val follower = Follower(userToFollow, userFollower)
            followerRepository.persist(follower)
        }

    }

    fun getAllByUserId(userId: Long): FollowersResponse {
        val followersResponseList = userRepository.findById(userId)
            ?.let { followerRepository.findByUserId(userId) }
            ?: throw ResourceNotFoundException("Entity User not found for id: $userId")

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

    fun unfollowByUsersIds(userId: Long, toUnfollow: Long) {
        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("Entity User not found for id: $userId")

        val userToUnfollow = userRepository.findById(toUnfollow)
            ?: throw ResourceNotFoundException("Entity User not found for id: $toUnfollow")

        if(followerRepository.alreadyFollows(userToUnfollow, user))
            followerRepository.deleteByUserAndFollower(user, userToUnfollow)
        else
            throw ResourceNotFoundException("Invalid Follower ID: ${userToUnfollow.id}, for User ID: ${user.id}")
    }

    fun alreadyFollows(followerId: Long, userId: Long): Boolean {
        val user = userRepository.findById(userId)
            ?: throw ResourceNotFoundException("Entity User not found for id: $userId")

        val follower = userRepository.findById(followerId)
            ?: throw ResourceNotFoundException("Entity User not found for id: $followerId")

        return followerRepository.alreadyFollows(follower, user)
    }

}
