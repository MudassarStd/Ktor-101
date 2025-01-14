package com.ktor

import com.ktor.taskmanager.model.Priority
import com.ktor.taskmanager.model.Task
import com.ktor.taskmanager.model.TaskRepository
import com.ktor.taskmanager.model.tasksAsTable
import io.ktor.http.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Application.configureRouting() {
    routing {

        staticResources("/task-ui", "task-ui")

        // post routes
        post("/tasks") {
            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: ""
            )
            println("Data received: $params")

            if (params.toList().any {it.isEmpty()}) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                val priority = Priority.valueOf(params.third.uppercase(Locale.getDefault()))

                TaskRepository.addTask(
                    Task(
                        name = params.first,
                        description = params.second,
                        priority = priority,
                    )
                )

                call.respond(HttpStatusCode.NoContent)
                call.respondRedirect("/")

            } catch (e: Exception) {
                println(e.message)
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/tasks") {
            call.respondText(contentType = ContentType.parse("text/html"),
                text = """"
                     <h3>TODO:</h3>
                <ol>
                    <li>A table of all the tasks</li>
                    <li>A form to submit new tasks</li>
                </ol>
                """.trimIndent()
            )
        }

        get {
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }

        get("tasks/byPriority/{priority}") {
            val priorityAsText = call.parameters["priority"]
            if (priorityAsText == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {
                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText (
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }


        }

//        get("/test1") {
//            val text = "<h1> Hello from Ktor project</h1>"
//            val type = ContentType.parse("text/html")
//            call.respondText(text, type)
//        }
    }
}
