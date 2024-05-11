package org.israelgda.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.israelgda.dto.PostDTO
import org.israelgda.repositories.PostRepository
import org.israelgda.repositories.UserRepository
import org.israelgda.utils.toDTO
import org.israelgda.utils.toEntity

@ApplicationScoped
class PostService {

    @Inject
    lateinit var postRepository: PostRepository

    @Inject
    lateinit var userRepository: UserRepository

    fun getAllByUserId(userId: Long): List<PostDTO> {

        return userRepository.findById(userId)
            ?.let { userEntity ->
                postRepository.find("user", userEntity)
                    .list()
                    .map { it.toDTO() }
            } ?: throw RuntimeException("Entity User not found for id: $userId")
    }

    @Transactional
    fun create(userId: Long, postDTO: PostDTO): PostDTO {
        val userEntity = userRepository.findById(userId)

        if(userEntity != null) {
            val postEntity = postDTO.toEntity(userEntity)
            postRepository.persist(postEntity)

            return postEntity.toDTO()
        } else {
            throw RuntimeException("Entity User not found for id: $userId")
        }
    }

}
