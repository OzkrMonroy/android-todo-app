package com.oscar.monroy.todoapp.addtasks.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oscar.monroy.todoapp.addtasks.domain.AddTaskUseCase
import com.oscar.monroy.todoapp.addtasks.domain.GetTasksUseCase
import com.oscar.monroy.todoapp.addtasks.ui.TasksUIState.*
import com.oscar.monroy.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val addTaskUseCase: AddTaskUseCase, getTasksUseCase: GetTasksUseCase):ViewModel() {
    val uiState: StateFlow<TasksUIState> = getTasksUseCase().map(::Success)
        .catch{ Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    //private val _tasks = mutableStateListOf<TaskModel>()
    //val tasks: List<TaskModel> = _tasks

    fun onDismissDialog() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String){
        onDismissDialog()
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialog() {
        _showDialog.value = true
    }

    fun onCheckboxSelected(taskModel: TaskModel) {
        //val index = _tasks.indexOf(taskModel)
        //_tasks[index] = _tasks[index].let {
          //  it.copy(selected = !it.selected)
        //}
    }

    fun onRemoveItem(taskModel: TaskModel) {
        //val task = _tasks.find { it.id == taskModel.id }
        //_tasks.remove(task)
    }
}