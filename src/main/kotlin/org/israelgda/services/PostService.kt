package org.israelgda.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.israelgda.dto.PostDTO
import org.israelgda.model.Post
import org.israelgda.model.User
import org.israelgda.repositories.PostRepository
import org.israelgda.repositories.UserRepository

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
                    .map { entityToDTO(it) }
            } ?: throw RuntimeException("Entity User not found for id: $userId")
    }

    @Transactional
    fun create(userId: Long, postDTO: PostDTO): PostDTO {
        val userEntity = userRepository.findById(userId)

        if(userEntity != null) {
            val postEntity = dtoToEntity(userEntity, postDTO)
            postRepository.persist(postEntity)

            return entityToDTO(postEntity)
        } else {
            throw RuntimeException("Entity User not found for id: $userId")
        }
    }

    private fun entityToDTO(post: Post) = PostDTO(post.post, post.postdate)

    private fun dtoToEntity(user: User, postDTO: PostDTO) = Post(postDTO.post, user)


}
