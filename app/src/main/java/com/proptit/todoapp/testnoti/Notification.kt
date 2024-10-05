package com.proptit.todoapp.testnoti

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.proptit.todoapp.model.Task
import java.util.Calendar

object Notification {
  /*  fun notificationTask(context: Context, task: Task) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTime = System.currentTimeMillis()

        // Kiểm tra xem dueDate có phải là hôm nay không
        val calendarDueDate = Calendar.getInstance().apply {
            time = task.dueDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val calendarToday = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Kiểm tra nếu dueDate là ngày hôm nay
        if (calendarDueDate.timeInMillis == calendarToday.timeInMillis) {
            // Thời gian đến hạn dự kiến
            val timeNotify = task.dueTime.time

            // Tính thời gian còn lại đến dueTime
            val timeLeft = timeNotify - currentTime

            // Kiểm tra nếu còn 30 phút (1800000 milliseconds)
            if (timeLeft > 0 && timeLeft <= 30 * 60 * 1000) {
                val intent = Intent(context, AlarmReceiver::class.java).apply {
                    putExtra("title", task.title)
                    putExtra("description", task.description)
                    putExtra("id", task.id)
                }

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    task.id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeNotify, pendingIntent)
            }
        }
    }*/
    fun notificationTask(context: Context, task: Task){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val timeNotify = task.dueDate.time

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("title", task.title)
            putExtra("description", task.description)
            putExtra("id", task.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, task.id.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeNotify, pendingIntent)
    }


}