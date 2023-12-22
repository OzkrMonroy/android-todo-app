package com.oscar.monroy.todoapp.addtasks.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.oscar.monroy.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class TaskViewModel @Inject constructor():ViewModel() {
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasks: List<TaskModel> = _tasks

    fun onDismissDialog() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String){
        onDismissDialog()
        _tasks.add(TaskModel(task = task))
    }

    fun onShowDialog() {
        _showDialog.value = true
    }

    fun onCheckboxSelected(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onRemoveItem(taskModel: TaskModel) {
        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)
    }
}