package com.carlostorres.routes

import com.carlostorres.models.ApiResponse
import com.carlostorres.repository.HeroRepo
import com.carlostorres.repository.HeroRepoImpl
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import java.lang.IllegalArgumentException

fun Route.getAllHeroes() {

    val heroRepo : HeroRepo by inject()

    get("/boruto/heroes") {

        try {

            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            require(page in 1..5)

            val apiResponse = heroRepo.getAllHeroes(page = page)

            call.respond(
                message = apiResponse,
                status = HttpStatusCode.OK
            )

        } catch (e: NumberFormatException) {

            call.respond(
                message = ApiResponse(
                    success = false,
                    message = "Only Numbers Allowed"
                ),
                status = HttpStatusCode.BadRequest
            )

        } catch (e: IllegalArgumentException){

            call.respond(
                message = ApiResponse(
                    success = false,
                    message = "Heroes not found"
                ),
                status = HttpStatusCode.NotFound
            )

        }

    }
}