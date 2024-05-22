package org.israelgda.resources

import io.quarkus.test.InjectMock
import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus.*
import org.hamcrest.Matchers
import org.israelgda.dto.UserDTO
import org.israelgda.model.User
import org.israelgda.services.UserService
import org.israelgda.services.exceptions.ResourceNotFoundException
import org.israelgda.utils.toDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.net.URL


@QuarkusTest
class UserResourceTest {

    @TestHTTPResource("/users")
    private lateinit var apiURL: URL

    @InjectMock
    lateinit var userService: UserService

    @Test
    fun requestDeCriacaoDeUsuarioDeveRetornarResponse201EBodyCriado() {
        val user = User("Fulano", 20).toDTO()

        `when`(userService.create(user))
            .thenReturn(UserDTO(1, "Fulano", 20))

        val response =
        given()
            .contentType(ContentType.JSON)
            .body(user)
        .`when`()
            .post(apiURL)
        .then()
            .extract()
            .response()

        assertEquals(SC_CREATED, response.statusCode, "O response code deveria ser 201")
        assertEquals("Fulano", response.jsonPath().getString("name"))
        assertEquals("20", response.jsonPath().getString("age"))
    }

    @Test
    fun requestDeCriacaoDeUsuarioComDadosInvalidosDeveRetornarResponse422EMensagemDeErro() {
        val userSemNome = UserDTO(0, null, 100)

        val responseErroSemNome =
            given()
                .contentType(ContentType.JSON)
                .body(userSemNome)
            .`when`()
                .post(apiURL)
            .then()
                .extract()
                .response()

        assertEquals(SC_UNPROCESSABLE_ENTITY, responseErroSemNome.statusCode, "O response code deveria ser 422")
        assertEquals("Validation Error", responseErroSemNome.jsonPath().getString("message"))
        assertEquals("name", responseErroSemNome.jsonPath().get("errors[0].field"))
        assertEquals("Name is required", responseErroSemNome.jsonPath().get("errors[0].message"))

        val userSemIdade = UserDTO(0, "Chico Moedas", null)

        val responseErroSemIdade =
            given()
                .contentType(ContentType.JSON)
                .body(userSemIdade)
            .`when`()
                .post(apiURL)
            .then()
                .extract()
                .response()

        assertEquals(SC_UNPROCESSABLE_ENTITY, responseErroSemIdade.statusCode, "O response code deveria ser 422")
        assertEquals("Validation Error", responseErroSemIdade.jsonPath().getString("message"))
        assertEquals("age", responseErroSemIdade.jsonPath().get("errors[0].field"))
        assertEquals("Age is required", responseErroSemIdade.jsonPath().get("errors[0].message"))
    }

    @Test
    fun requestDeConsultaDeUsuarioPorIdDeveRetornarResponse200EBodyDeRetorno() {
        `when`(userService.findById(anyLong()))
            .thenReturn(UserDTO(1, "Fulano", 20))

        val response =
            given()
                .contentType(ContentType.JSON)
            .`when`()
                .get("/users/{id}", 1)
            .then()
                .extract()
                .response()

        assertEquals(SC_OK, response.statusCode, "O response code deveria ser 200")
        assertEquals("1", response.jsonPath().getString("id"))
        assertEquals("Fulano", response.jsonPath().getString("name"))
        assertEquals("20", response.jsonPath().getString("age"))
    }

    @Test
    fun requestDeConsultaDeUsuarioDeveRetornarResponse200EBodyDeRetornoComListaDeUsuarios() {
        `when`(userService.getAll())
            .thenReturn(listOf(User("Fulano", 20).toDTO()))

        val response = given()
            .contentType(ContentType.JSON)
        .`when`()
            .get(apiURL)
        .then()
            .statusCode(200)
            .body("size()", Matchers.`is`(1))
            .extract()
            .response()

        assertEquals("Fulano", response.jsonPath().getString("[0].name"))
        assertEquals("20", response.jsonPath().getString("[0].age"))
    }

    @Test
    fun requestDeConsultaDeUsuarioPorIdInvalidoDeveRetornarResponse404EMensagemDeErro() {
        `when`(userService.findById(anyLong()))
            .thenThrow(ResourceNotFoundException("Entity not found"))

        val response =
            given()
                .contentType(ContentType.JSON)
                .`when`()
                .get("/users/{id}", 999)
                .then()
                .extract()
                .response()

        assertEquals(SC_NOT_FOUND, response.statusCode, "O response code deveria ser 404")
        assertEquals("Entity not found", response.jsonPath().getString("message"))
    }

    @Test
    fun requestDeUpdateDeUsuarioDeveRetornarResponse204() {
        val user = User( "Fulano", 20).toDTO()

        `when`(userService.update(1L, user))
            .thenReturn(User("Fulano", 20).toDTO())

        given()
            .contentType(ContentType.JSON)
            .body(user)
        .`when`()
            .put("/users/{id}", 1L)
        .then()
            .statusCode(204)
    }

    @Test
    fun requestDeUpdateDeUsuarioPorIdInvalidoDeveRetornarResponse404() {
        val user = User("Fulano", 20).toDTO()

        `when`(userService.update(999L, user))
            .thenThrow(ResourceNotFoundException("Entity not found"))

        given()
            .contentType(ContentType.JSON)
            .body(user)
        .`when`()
            .put("/users/{id}", 999L)
        .then()
            .statusCode(404)
    }

    @Test
    fun requestDeDeleteDeUsuarioDeveRetornarResponse204() {

        doNothing()
            .`when`(userService)
            .delete(anyLong())

        given()
            .contentType(ContentType.JSON)
        .`when`()
            .delete("/users/{id}", 1L)
        .then()
            .statusCode(204)
    }
    @Test
    fun requestDeDeleteDeUsuarioComIdInvalidoDeveRetornarResponse404() {

        doThrow(ResourceNotFoundException("Entity Not Found"))
            .`when`(userService)
            .delete(anyLong())

        val response =
        given()
            .contentType(ContentType.JSON)
        .`when`()
            .delete("/users/{id}", 1L)
        .then()
            .statusCode(404)
            .extract()
            .response()

        assertEquals("Entity Not Found", response.jsonPath().getString("message"))

    }
}