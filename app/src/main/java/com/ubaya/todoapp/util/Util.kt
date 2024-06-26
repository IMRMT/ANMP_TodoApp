package com.ubaya.todoapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ubaya.todoapp.R
import com.ubaya.todoapp.model.TodoDatabase
import com.ubaya.todoapp.view.MainActivity

val DB_NAME = "newtododb"

fun buildDb(context:Context):TodoDatabase{
    val db =TodoDatabase.buildDatabase(context)
    return db
}

val MIGRATION_1_3 = object: Migration(1,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null")
        database.execSQL("ALTER TABLE todo ADD is_done priority INTEGER DEFAULT 0 not null")
    }

}

val MIGRATION_3_4 = object: Migration(3,4){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN todo_date INTEGER DEFAULT 0 not null")
    }

}

class TodoWorker(context: Context, params:WorkerParameters): Worker(context,params) {

    override fun doWork(): Result {
        val judul = inputData.getString("title").toString()
        val msg = inputData.getString("message").toString()
        NotificationHelper(applicationContext).createNotification(judul,msg)
        return Result.success()
    }
}


class NotificationHelper(val context: Context){
    private val CHANNEL_ID = "todo_channel"
    private val NOTIFICATION_ID = 1

    companion object {
        val REQUEST_NOTIF = 100
    }

    private fun createNotificationChannel(){
        val channel = NotificationChannel(CHANNEL_ID,CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel to publish a notification when todo created"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification(title:String, message: String){
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.checklist)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)

            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, notif)
        } catch (e:SecurityException) {
            Log.e("error", e.toString())
        }
    }
}

