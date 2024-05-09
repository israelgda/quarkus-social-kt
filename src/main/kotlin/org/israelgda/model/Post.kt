package org.israelgda.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "posts")
class Post (
    post: String,
    user: User
) {

    constructor(): this("", User())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var post: String = post
    var postdate: Instant = Instant.now()

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User = user
}