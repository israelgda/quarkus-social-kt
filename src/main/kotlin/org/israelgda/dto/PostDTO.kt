package org.israelgda.dto

import jakarta.validation.constraints.NotBlank
import java.time.Instant

class PostDTO (
    postText: String,
    postDate: Instant
) {

    @NotBlank(message = "Post text is required")
    val postText = postText

    val postDate = postDate
}