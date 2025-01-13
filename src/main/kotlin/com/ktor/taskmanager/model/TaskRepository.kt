package com.ktor.taskmanager.model

object TaskRepository {
    private val tasks = mutableListOf(
        Task("cleaning", "Clean the house", Priority.LOW),
        Task("gardening", "Mow the lawn", Priority.MEDIUM),
        Task("shopping", "Buy the groceries", Priority.HIGH),
        Task("painting", "Paint the fence", Priority.VITAL)
    )

    fun allTasks(): List<Task> = tasks

    fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    fun tasksByName(name: String) = tasks.find {
        it.name == name
    }

    fun addTask(task: Task) {
        if (tasksByName(task.name) != null) throw IllegalStateException("Duplicate Task Names")
        tasks.add(task)
    }
}
