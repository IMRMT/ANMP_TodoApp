package com.ubaya.todoapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.todoapp.model.Todo
import com.ubaya.todoapp.model.TodoDatabase
import com.ubaya.todoapp.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetailTodoViewModel(application: Application):AndroidViewModel(application), CoroutineScope {
    private var job = Job()
    val todoLD = MutableLiveData<Todo>()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun fetch(uuid: Int){
        launch {
            val db = buildDb(getApplication()).todoDao().selectTodo(uuid)
        }
    }

    fun update(todo:Todo) {
        launch {
            buildDb(getApplication()).todoDao().updateTodo(todo)
        }
    }

    fun addTodo(todo: Todo){
        launch {
            val db = TodoDatabase.buildDatabase(getApplication())
            db.todoDao().insertAll(todo)
        }
    }
}