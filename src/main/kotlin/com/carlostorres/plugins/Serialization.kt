package com.carlostorres.plugins

import io.ktor.features.*
import io.ktor.response.*
import io.ktor.serialization.*
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
