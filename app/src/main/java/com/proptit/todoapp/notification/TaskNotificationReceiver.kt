/*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.proptit.todoapp.R

class TaskNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("taskId", -1)
        val taskTitle = intent.getStringExtra("taskTitle")

        if (taskId != -1 && taskTitle != null) {
            // Xây dựng thông báo
            val notification = NotificationCompat.Builder(context, "TASK_CHANNEL_ID")
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Task Reminder")
                .setContentText("Your task '$taskTitle' is due in 30 minutes!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build()

            // Hiển thị thông báo
            with(NotificationManagerCompat.from(context)) {
                notify(taskId, notification)
            }
        }
    }
}
*/

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.proptit.todoapp.MainActivity
import com.proptit.todoapp.R

class TaskNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getIntExtra("taskId", -1)
        val taskTitle = intent.getStringExtra("taskTitle") ?: "No Title"

        // Tạo thông báo
        createNotification(context, taskId, taskTitle)
    }

    private fun createNotification(context: Context, taskId: Int, taskTitle: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_notifications"

        // Tạo kênh thông báo (cho Android Oreo trở lên)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Task Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("taskId", taskId) // Đưa taskId để mở chi tiết task
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Task Reminder")
            .setContentText(taskTitle)
            .setSmallIcon(R.drawable.app_icon) // Thay thế bằng icon của bạn
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(taskId, notification)
    }
}

