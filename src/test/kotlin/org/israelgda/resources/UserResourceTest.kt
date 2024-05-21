package org.israelgda.resources

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus
import org.israelgda.dto.UserDTO
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

        val response =
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .`when`()
            .post("/users")
        .then()
            .extract()
            .response()

        assertEquals(HttpStatus.SC_CREATED, response.statusCode,
            "O response code deveria ser 200")
        assertNotNull(response.jsonPath().getString("id"))
        assertEquals(response.jsonPath().getString("name"), "Fulano")
        assertEquals(response.jsonPath().getString("age"), "20")
    }

    @Test
    fun requestDeCriacaoDeUsuarioComDadosInvalidosRetornarResponse422EMensagemDeErro() {
        val userSemNome = UserDTO(0, null, 100)

        val responseErroSemNome =
            given()
                .contentType(ContentType.JSON)
                .body(userSemNome)
                .`when`()
                .post("/users")
                .then()
                .extract()
                .response()

        assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, responseErroSemNome.statusCode,
            "O response code deveria ser 422")
        assertEquals("Validation Error", responseErroSemNome.jsonPath().getString("message"))
        assertEquals("name", responseErroSemNome.jsonPath().get("errors[0].field"))
        assertEquals("Name is required", responseErroSemNome.jsonPath().get("errors[0].message"))

        val userSemIdade = UserDTO(0, "Chico Moedas", null)

        val responseErroSemIdade =
            given()
                .contentType(ContentType.JSON)
                .body(userSemIdade)
                .`when`()
                .post("/users")
                .then()
                .extract()
                .response()

        assertEquals(HttpStatus.SC_UNPROCESSABLE_ENTITY, responseErroSemIdade.statusCode,
            "O response code deveria ser 422")
        assertEquals("Validation Error", responseErroSemIdade.jsonPath().getString("message"))
        assertEquals("age", responseErroSemIdade.jsonPath().get("errors[0].field"))
        assertEquals("Age is required", responseErroSemIdade.jsonPath().get("errors[0].message"))
    }
}