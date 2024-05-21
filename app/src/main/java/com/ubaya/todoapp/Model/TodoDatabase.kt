package com.ubaya.todoapp.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ubaya.todoapp.Util.DB_NAME
import com.ubaya.todoapp.Util.MIGRATION_1_2

@Database(entities = arrayOf(Todo::class), version = 2)//harus didaftarkan di build database
abstract class TodoDatabase:RoomDatabase() {
    abstract fun todoDao():TodoDao

    companion object{
        @Volatile private var instance:TodoDatabase ?= null
        private val Lock = Any()

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, TodoDatabase::class.java, DB_NAME
        )   .addMigrations(MIGRATION_1_2)
            .build()

        operator fun invoke(context: Context){
            if(instance!= null){
                synchronized(Lock){
                    instance?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}