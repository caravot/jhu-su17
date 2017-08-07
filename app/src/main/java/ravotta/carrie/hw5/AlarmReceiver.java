package ravotta.carrie.hw5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		ArrayList<TodoItem> todoItems = Util.findDueTodos(context);

        Log.d("onReceive", Boolean.toString(todoItems == null));
        if (todoItems != null) {
            Intent intent1 = new Intent(context, TodoListActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(todoItems.size() + " Items Due")
                    .setContentText("Yippie!")
                    .setSmallIcon(R.drawable.ic_add_alert_24dp)
                    .setContentIntent(pendingIntent)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        }
	}
}
