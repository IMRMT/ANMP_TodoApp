package com.ubaya.todoapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ubaya.todoapp.Model.Todo
import com.ubaya.todoapp.Model.TodoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListTodoViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    val todoLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()
    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    fun refresh(){
        loadingLD.value = true
        todoLoadErrorLD.value = false

        launch {
            val db = TodoDatabase.buildDatabase(getApplication())

            todoLD.postValue(db.todoDao().selectAllTodo())
            loadingLD.postValue(false)
        }
    }

    fun checkTask(todo: Todo){
        launch {
            val db = TodoDatabase.buildDatabase(getApplication())
            db.todoDao().deleteTodo(todo)
            todoLD.postValue(db.todoDao().selectAllTodo())
        }
    }
}