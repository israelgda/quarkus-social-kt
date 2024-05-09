package org.israelgda.dto

import jakarta.validation.constraints.NotBlank
import java.time.Instant

class PostDTO (
    post: String,
    postdate: Instant
) {

    @NotBlank(message = "Post text is required")
    val post = post

    val postdate = postdate
}