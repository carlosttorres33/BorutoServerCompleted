package com.carlostorres

import com.carlostorres.models.ApiResponse
import com.carlostorres.repository.HeroRepo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Test
import org.koin.java.KoinJavaComponent.inject
import kotlin.test.assertEquals

const val NEXT_PAGE_KEY = "nextPage"
const val PREV_PAGE_KEY = "prevPage"

class ApplicationTest {

    private val heroRepo: HeroRepo by inject(HeroRepo::class.java)

    @Test
    fun `access all heroes endpoint, query non existing page number, assert error`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes?page=6").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )
                val expected = ApiResponse(
                    success = false,
                    message = "Heroes not found"
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())

                println("Expected: $actual")
                println("Actual: $expected")

                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access all heroes endpoint, query invalid page number, assert error`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes?page=invalid").apply {
                assertEquals(
                    expected = HttpStatusCode.BadRequest,
                    actual = response.status()
                )
                val expected = ApiResponse(
                    success = false,
                    message = "Only Numbers Allowed"
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())

                println("Expected: $actual")
                println("Actual: $expected")

                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun `access all heroes enpoint, query all pages, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            val pages = 1..5
            val heroes = listOf(
                heroRepo.page1,
                heroRepo.page2,
                heroRepo.page3,
                heroRepo.page4,
                heroRepo.page5,
            )
            pages.forEach { page ->
                handleRequest(HttpMethod.Get, "/boruto/heroes?page=$page").apply {
                    println("Current page: $page")
                    assertEquals(
                        expected = HttpStatusCode.OK,
                        actual = response.status()
                    )
                    val expected = ApiResponse(
                        success = true,
                        message = "OK",
                        prevPage = calculatePage(page)[PREV_PAGE_KEY],
                        nextPage = calculatePage(page)[NEXT_PAGE_KEY],
                        heroes = heroes[page-1]
                    )
                    val actual = Json.decodeFromString<ApiResponse>(response.content.toString())

                    println("PrevPage: ${calculatePage(page)[PREV_PAGE_KEY]}")
                    println("NextPage: ${calculatePage(page)[NEXT_PAGE_KEY]}")
                    println("Heroes: ${heroes[page-1]}")

                    assertEquals(
                        expected = expected,
                        actual = actual
                    )
                }
            }
        }
    }

    private fun calculatePage(page : Int) : Map<String,Int?>{

        var prevPage : Int? = page
        var nextPage : Int? = page

        if (page in 1..4){
            nextPage = nextPage?.plus(1)
        }
        if (page in 2..5){
            prevPage = prevPage?.minus(1)
        }
        if (page == 1){
            prevPage = null
        }
        if (page == 5){
            nextPage = null
        }

        return mapOf(
            PREV_PAGE_KEY to prevPage,
            NEXT_PAGE_KEY to nextPage
        )
    }

    @Test
    fun `access search heroes endpoint, query hero name, assert single hero result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=sas").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes.size

                println("Expected: $actual")

                assertEquals(
                    expected = 1,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint, query hero name, assert multiple hero result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=sa").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes.size

                println("Expected: $actual")

                assertEquals(
                    expected = 3,
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint, query an empty text, assert empty list as a result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes

                println("Expected: $actual")

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access search heroes endpoint, query non existing hero, assert empty list as a result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/boruto/heroes/search?name=Carlos").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    .heroes

                println("Expected: $actual")

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @Test
    fun `access non existing endpoint, assert not found`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/unknown").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )

                assertEquals(
                    expected = "Page Not Found",
                    actual = response.content
                )
            }
        }
    }

}
