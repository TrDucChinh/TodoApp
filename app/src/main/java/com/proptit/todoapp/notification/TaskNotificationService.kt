import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.proptit.todoapp.database.TodoDatabase
import com.proptit.todoapp.database.task.TaskDao
import com.proptit.todoapp.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TaskNotificationService : LifecycleService() {

    private lateinit var taskDao: TaskDao

    override fun onCreate() {
        super.onCreate()

        // Khởi tạo Room Database DAO
        taskDao = TodoDatabase.getDatabase(this).taskDao()

        // Đặt thông báo cho các task chưa hoàn thành
        scheduleNotificationsForTasks()
    }

    private fun scheduleNotificationsForTasks() {
        lifecycleScope.launch {
            // Lấy danh sách các task chưa hoàn thành từ RoomDatabase
            val unfinishedTasks = withContext(Dispatchers.IO) {
                taskDao.getAllUncompletedTasksDirectly()
            }
            Log.e("TaskNotificationService", "Scheduling notifications for tasks: ${unfinishedTasks.size}")


            // Lấy thời gian hiện tại
            val currentDate = Calendar.getInstance().time

            // Kiểm tra và đặt thông báo cho mỗi task
            unfinishedTasks.forEach { task ->
                if (isSameDay(task.dueDate, currentDate)) {
                    scheduleTaskNotification(this@TaskNotificationService, task)
                }
            }
        }
    }

    private fun isSameDay(taskDate: Date, currentDate: Date): Boolean {
        val calendar1 = Calendar.getInstance().apply { time = taskDate }
        val calendar2 = Calendar.getInstance().apply { time = currentDate }

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    private fun scheduleTaskNotification(context: Context, task: Task) {
        val currentTime = Calendar.getInstance().time

        // Kiểm tra task có cùng ngày với ngày hiện tại không
        if (!isSameDay(task.dueDate, currentTime)) {
            return // Không đặt thông báo nếu không cùng ngày
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra("taskId", task.id)
            putExtra("taskTitle", task.title)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationTime = Calendar.getInstance().apply {
            time = task.dueTime
            add(Calendar.MINUTE, -30) // Đặt thông báo trước 30 phút
        }.timeInMillis

        // Kiểm tra nếu còn ít nhất 30 phút
        if (notificationTime > currentTime.time) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                notificationTime,
                pendingIntent
            )
        }
    }


}
