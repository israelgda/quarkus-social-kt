package org.israelgda.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.israelgda.dto.UserDTO
import org.israelgda.model.User
import org.israelgda.repositories.UserRepository

@ApplicationScoped
class UserService {

    @Inject
    lateinit var userRepository: UserRepository

    fun findById(id: Long): UserDTO {

        return userRepository.findById(id)
            ?.let { entityToDTO(it) }
            ?: throw RuntimeException("Entity User not found for id: $id")
    }

    fun getAll(): List<UserDTO> {
        return userRepository.findAll().list().map { entityToDTO(it) }
    }

    @Transactional
    fun create(user: UserDTO): UserDTO {
        val userEntity = dtoToEntity(user)
        userRepository.persist(userEntity)

        return entityToDTO(userEntity)
    }

    @Transactional
    fun update(id: Long, userDto: UserDTO): UserDTO {
        val userEntity = userRepository.findById(id)

        if(userEntity != null) {
            val userUpdated = updateEntity(userEntity, userDto)
            return entityToDTO(userUpdated)
        } else {
             throw RuntimeException("Entity User not found for id: $id")
        }
    }

    private fun updateEntity(userUpdated: User, userDto: UserDTO): User {
        userUpdated.name = userDto.name
        userUpdated.age = userDto.age
        return userUpdated
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.findById(id)
            ?.let { userRepository.deleteById(id) }
            ?: throw RuntimeException("Entity User not found for id: $id")
    }

    private fun entityToDTO(user: User) = UserDTO(user.id, user.name, user.age)

    private fun dtoToEntity(user: UserDTO) = User(user.name, user.age)
}