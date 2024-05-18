package org.israelgda.model

import jakarta.persistence.*

@Entity
@Table(name = "followers")
class Follower (
    user: User,
    follower: User
){
    constructor(): this(User(), User())

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user = user

    @ManyToOne
    @JoinColumn(name = "follower_id")
    var follower = follower
}