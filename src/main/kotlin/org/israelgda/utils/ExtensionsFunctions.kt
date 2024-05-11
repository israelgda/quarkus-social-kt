package org.israelgda.utils

import org.israelgda.dto.PostDTO
import org.israelgda.dto.UserDTO
import org.israelgda.model.Post
import org.israelgda.model.User

fun User.toDTO() = UserDTO(this.id, this.name, this.age)

fun UserDTO.toEntity() = User(this.name, this.age)

fun Post.toDTO() = PostDTO(this.postText, this.postDate)

fun PostDTO.toEntity(user: User) = Post(this.postText, user)
