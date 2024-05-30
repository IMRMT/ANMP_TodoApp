package com.ubaya.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)
    @Query("Select * from todo ORDER BY priority DESC")
    fun selectAllTodo(): List<Todo>
    @Query("Select * from todo where uuid= :id")
    fun selectTodo(id:Int): Todo
    @Delete
    fun deleteTodo(todo: Todo)
    @Query("UPDATE todo SET title=:title, notes=:notes, priority=:priority WHERE uuid=:uuid")
    fun update(title: String, notes: String, priority: Int, uuid: Int)
    @Update
    fun updateTodo(todo: Todo)
}