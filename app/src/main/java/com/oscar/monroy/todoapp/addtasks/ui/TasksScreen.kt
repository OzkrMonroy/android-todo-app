package com.oscar.monroy.todoapp.addtasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.oscar.monroy.todoapp.addtasks.ui.model.TaskModel

@Composable
fun TasksScreen(taskViewModel: TaskViewModel) {
    val showDialog: Boolean by taskViewModel.showDialog.observeAsState(false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TasksUIState>(
        initialValue = TasksUIState.Loading,
        key1 = lifecycle,
        key2 = taskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TasksUIState.Error -> TODO()
        TasksUIState.Loading -> { CircularProgressIndicator() }
        is TasksUIState.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AddTaskDialog(
                    show = showDialog,
                    onDismiss = { taskViewModel.onDismissDialog() },
                    onTaskAdded = { taskViewModel.onTaskCreated(it) })
                NewTaskFab(Modifier.align(Alignment.BottomEnd), taskViewModel)
                TasksList((uiState as TasksUIState.Success).tasks, taskViewModel)
            }
        }
    }
}

@Composable
fun NewTaskFab(modifier: Modifier, taskViewModel: TaskViewModel) {
    FloatingActionButton(onClick = { taskViewModel.onShowDialog() }, modifier = modifier) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new task")
    }
}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember {
        mutableStateOf("")
    }
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add your task",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                OutlinedTextField(
                    value = myTask,
                    onValueChange = { myTask = it },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskAdded(myTask)
                        myTask = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add task")
                }
            }
        }
    }
}

@Composable
fun TasksList(tasks: List<TaskModel>, taskViewModel: TaskViewModel) {
    LazyColumn {
        items(tasks, key = { it -> it.id }) {
            TaskItem(taskModel = it, taskViewModel = taskViewModel)
        }
    }
}

@Composable
fun TaskItem(taskModel: TaskModel, taskViewModel: TaskViewModel) {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    taskViewModel.onRemoveItem(taskModel)
                })
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskModel.task, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = { taskViewModel.onCheckboxSelected(taskModel) })
        }

    }
}