package org.israelgda.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class UserDTO(
    id: Long,
    name: String,
    age: Int
) {

    val id: Long = id

    @NotBlank(message = "Name is required")
    val name: String = name

    @NotNull(message = "Age is required")
    val age: Int = age

}
