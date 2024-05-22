package org.israelgda.resources

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_UNPROCESSABLE_ENTITY
import org.hamcrest.Matchers
import org.israelgda.dto.PostDTO
import org.israelgda.services.FollowerService
import org.israelgda.services.PostService
import org.israelgda.services.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import java.time.Instant


@QuarkusTest
class PostResourceTest {


    private val apiURL = "/users/{userId}/posts"

    @InjectMock
    lateinit var userService: UserService

    @InjectMock
    lateinit var postService: PostService

    @InjectMock
    lateinit var followerService: FollowerService

    @Test
    fun requestDeCriacaoDePostDeveRetornarResponse201EBodyCriado() {

        val postDto = PostDTO("Post teste", Instant.now())

        `when`(postService.create(1L, postDto))
            .thenReturn(postDto)

        val response =
        given()
            .contentType(ContentType.JSON)
            .body(postDto)
        .`when`()
            .post(apiURL, 1L)
        .then()
            .extract()
            .response()

        assertEquals(SC_CREATED, response.statusCode, "O response code deveria ser 201")
        assertEquals("Post teste", response.jsonPath().getString("postText"))
        assertNotNull(response.jsonPath().getString("postDate"))
    }

    @Test
    fun requestDeCriacaoDePostComDadosInvalidosDeveRetornarResponse422EMensagemDeErro() {

        val postDto = PostDTO(null, Instant.now())

        `when`(postService.create(1L, postDto))
            .thenReturn(postDto)

        val response =
            given()
                .contentType(ContentType.JSON)
                .body(postDto)
            .`when`()
                .post(apiURL, 1L)
            .then()
                .extract()
                .response()

        assertEquals(SC_UNPROCESSABLE_ENTITY, response.statusCode, "O response code deveria ser 422")
        assertEquals("Validation Error", response.jsonPath().getString("message"))
        assertEquals("postText", response.jsonPath().get("errors[0].field"))
        assertEquals("Post text is required", response.jsonPath().get("errors[0].message"))
    }

    @Test
    fun requestDeConsultaDePostsDeUsuarioDeveRetornarResponse200EBodyDeRetornoComListaDePosts() {
        `when`(followerService.alreadyFollows(anyLong(), anyLong()))
            .thenReturn(true)

        `when`(postService.getAllByUserId(anyLong()))
            .thenReturn(listOf(PostDTO("Post 1", Instant.now())))

        val response =
        given()
            .contentType(ContentType.JSON)
            .header("followerId", 1L)
        .`when`()
            .get(apiURL, 1L)
        .then()
            .statusCode(200)
            .body("size()", Matchers.`is`(1))
            .extract()
            .response()

        assertEquals("Post 1", response.jsonPath().getString("[0].postText"))
        assertNotNull(response.jsonPath().getString("[0].postDate"))
    }

    @Test
    fun requestDeConsultaDePostsDeUsuarioComConsultaDeNaoSeguidorDeveRetornarResponse403() {
        `when`(followerService.alreadyFollows(anyLong(), anyLong()))
            .thenReturn(false)

        val response =
            given()
                .contentType(ContentType.JSON)
                .header("followerId", 1L)
            .`when`()
                .get(apiURL, 1L)
            .then()
                .statusCode(403)
    }

}