package com.carlostorres.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import javax.naming.AuthenticationException

fun Application.configureStatusPages(){
    install(StatusPages){
        status(HttpStatusCode.NotFound){
            call.respond(
                message = "Page Not Found",
                status = HttpStatusCode.NotFound
            )
        }
        /*exception<AuthenticationException> {
            call.respond(
                message = "We caught an exception!",
                status = HttpStatusCode.OK
            )
        }*/
    }
}