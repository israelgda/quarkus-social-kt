package org.israelgda.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    name: String,
    age: Int
) {

    constructor(): this("", 0)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var name: String = name

    var age: Int = age

}