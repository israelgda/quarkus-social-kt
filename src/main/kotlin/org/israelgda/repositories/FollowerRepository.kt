package org.israelgda.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Parameters
import jakarta.enterprise.context.ApplicationScoped
import org.israelgda.model.Follower
import org.israelgda.model.User

@ApplicationScoped
class FollowerRepository: PanacheRepository<Follower>{

    fun alreadyFollows(follower: User, user: User): Boolean {
        val params = Parameters
            .with("follower", follower)
            .and("user", user)
            .map()
        val query =  find("follower =:follower and user =: user", params)

        return query.firstResult()
            ?.let { true }
            ?: false
    }

    fun findByUserId(userId: Long): List<Follower> {
        return find("user.id", userId).list()
    }
}