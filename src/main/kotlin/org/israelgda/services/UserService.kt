package org.israelgda.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.israelgda.dto.UserDTO
import org.israelgda.model.User
import org.israelgda.repositories.UserRepository
import org.israelgda.services.exceptions.ResourceNotFoundException
import org.israelgda.utils.toDTO
import org.israelgda.utils.toEntity

@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun findById(id: Long): UserDTO {
        return userRepository.findById(id)
            ?.toDTO()
            ?: throw ResourceNotFoundException("Entity User not found for id: $id")
    }

    fun getAll(): List<UserDTO> {
        return userRepository.findAll().list().map { it.toDTO() }
    }

    @Transactional
    fun create(user: UserDTO): UserDTO {
        val userEntity = user.toEntity()
        userRepository.persist(userEntity)

        return userEntity.toDTO()
    }

    @Transactional
    fun update(id: Long, userDto: UserDTO): UserDTO {
        val userEntity = userRepository.findById(id)

        if(userEntity != null) {
            val userUpdated = updateEntity(userEntity, userDto)
            return userUpdated.toDTO()
        } else {
             throw ResourceNotFoundException("Entity User not found for id: $id")
        }
    }

    private fun updateEntity(userUpdated: User, userDto: UserDTO): User {
        userUpdated.name = userDto.name!!
        userUpdated.age = userDto.age!!
        return userUpdated
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.findById(id)
            ?.let { userRepository.deleteById(id) }
            ?: throw ResourceNotFoundException("Entity User not found for id: $id")
    }

}