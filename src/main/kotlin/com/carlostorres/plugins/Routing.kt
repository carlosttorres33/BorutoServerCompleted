package com.carlostorres.plugins

import com.carlostorres.routes.root
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        root()
    }
}

