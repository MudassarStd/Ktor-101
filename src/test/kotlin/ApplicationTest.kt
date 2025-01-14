package com.ktor

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun tasksCanByFoundByPriority() = testApplication {
        application {
            module()
        }

        val response = client.get("/tasks/byPriority/VITAL")
        val body = response.bodyAsText()

        assertEquals(HttpStatusCode.NotFound, response.status)
    }


    // --------------- POST tests ------------------
    @Test
    fun newTasksCanBeAdded() = testApplication {
        application {
            module()
        }

        val response1 = client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "test Name",
                    "description" to "go to beach by user1",
                    "priority" to "LOfsdf",
                ).formUrlEncode()
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response1.status)
    }
}

