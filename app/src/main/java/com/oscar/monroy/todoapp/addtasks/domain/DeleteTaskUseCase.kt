package com.oscar.monroy.todoapp.addtasks.domain

import com.oscar.monroy.todoapp.addtasks.data.TaskRepository
import com.oscar.monroy.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val taskRepository: TaskRepository){
    suspend operator fun invoke(taskModel: TaskModel){
        taskRepository.delete(taskModel)
    }
}