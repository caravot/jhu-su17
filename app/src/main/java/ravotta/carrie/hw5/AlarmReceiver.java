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
    private static Notification notification;
    private static NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;

	public void onReceive(Context context, Intent intent) {
		ArrayList<TodoItem> todoItems = Util.findDueTodos(context);

        if (todoItems != null) {
            NotificationCompat.InboxStyle details = new NotificationCompat.InboxStyle()
                    .setBigContentTitle("Item List")
                    .setSummaryText(todoItems.size() + " Items Due");

            for (int i = 0; i < Math.min(5, todoItems.size()); i++) {
                details.addLine("Item " + (i + 1) + " : " + todoItems.get(i).name.get());

                // set item as due
                if (todoItems.get(i).status.get() != Status.DUE) {
                    todoItems.get(i).status.set(Status.DUE);
                    Util.updateTodo(context, todoItems.get(i));
                }
            }

            PendingIntent mainAction = createPending(context, 0, "View App");
            PendingIntent snoozeAction = createPending(context, 3, "Snooze All");
            PendingIntent clearAction = createPending(context, 4, "All Done");

            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(todoItems.size() + " Items Due")
                    .setStyle(details)
                    .setContentIntent(mainAction)
                    .addAction(R.drawable.ic_snooze_24dp, "Snooze All", snoozeAction)
                    .addAction(R.drawable.ic_clear_24dp, "Mark All as Done", clearAction)
                    .setSmallIcon(R.drawable.ic_add_alert_24dp)
                    .setAutoCancel(true)
                    .build();

            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else if (notification != null) {
            notificationManager.cancel(NOTIFICATION_ID);
        }
	}

    private PendingIntent createPending(Context context, int id, String info) {
        Intent intent = new Intent("ravotta.carrie.hw5.itemsdue2");
        intent.putExtra("actionId", id);

        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
