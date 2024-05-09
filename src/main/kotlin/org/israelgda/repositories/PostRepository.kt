package org.israelgda.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import org.israelgda.model.Post

@ApplicationScoped
class PostRepository: PanacheRepository<Post> {
}