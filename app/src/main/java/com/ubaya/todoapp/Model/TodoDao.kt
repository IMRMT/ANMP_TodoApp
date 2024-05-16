package com.ubaya.todoapp.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)
    @Query("Select * from todo")
    fun selectAllTodo(): List<Todo>
    @Query("Select * from todo where uuid= :id")
    fun selectTodo(id:Int): Todo
    @Delete
    fun deleteTodo(todo: Todo)
}