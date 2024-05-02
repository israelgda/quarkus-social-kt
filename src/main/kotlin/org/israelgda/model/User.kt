package org.israelgda.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    name: String,
    age: Int
): PanacheEntityBase {

    constructor(): this("", 0)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var name: String = name

    var age: Int = age

    companion object: PanacheCompanion<User>

}