package com.ktor.taskmanager.model

data class Task(
    val name: String,
    val description: String,
    val priority: Priority
)

// Extensions

fun Task.taskAsRow() = """
    <tr>
        <td>$name</td><td>$description</td><td>$priority</td>
    </tr>
""".trimIndent()



fun List<Task>.tasksAsTable() = this.joinToString(
    prefix = "<table rules=\"all\">",
    postfix = "</table>",
    separator = "\n",
    transform = Task::taskAsRow
)