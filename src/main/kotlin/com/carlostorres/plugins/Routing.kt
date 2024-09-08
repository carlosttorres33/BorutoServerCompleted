package com.carlostorres.plugins

import com.carlostorres.routes.getAllHeroes
import com.carlostorres.routes.root
import com.carlostorres.routes.searchHeroes
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        root()
        getAllHeroes()
        searchHeroes()

        static ("/images") {
            resources("images")
        }
    }
}

