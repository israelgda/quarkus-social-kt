package org.israelgda.dto

class FollowersResponse (
    followersCount: Int,
    followers: List<UserFollowerResponse>
) {

    constructor(): this(0, ArrayList())

    val followersCount = followersCount
    val followers = followers
}