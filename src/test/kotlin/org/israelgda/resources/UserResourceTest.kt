package org.israelgda.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.israelgda.model.User
import org.israelgda.utils.toDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@QuarkusTest
class UserResourceTest {

    @Test
    fun requestDeCriacaoDeUsuarioDeveRetornarResponse201EBodyCriado() {
        val user = User("Fulano", 20).toDTO()

        var response =
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .`when`()
            .post("/users")
        .then()
            .extract()
            .response();

        assertEquals(HttpStatus.SC_CREATED, response.statusCode,
            "O response code deveria ser 200")
        assertNotNull(response.jsonPath().getString("id"))
        assertEquals(response.jsonPath().getString("name"), "Fulano")
        assertEquals(response.jsonPath().getString("age"), "20")
    }
}