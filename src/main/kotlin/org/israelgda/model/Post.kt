package org.israelgda.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "posts")
class Post (
    postText: String,
    user: User
) {

    constructor(): this("", User())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "post_text")
    var postText: String = postText

    @Column(name = "post_date")
    lateinit var postDate: Instant

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User = user

    @PrePersist
    fun insertPostDate(){
        postDate = Instant.now()
    }
}