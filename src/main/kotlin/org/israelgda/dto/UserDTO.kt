package org.israelgda.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class UserDTO(
    id: Long,
    name: String,
    age: Int
) {

    val id = id

    @NotBlank(message = "Name is required")
    val name = name

    @NotNull(message = "Age is required")
    val age = age

}
