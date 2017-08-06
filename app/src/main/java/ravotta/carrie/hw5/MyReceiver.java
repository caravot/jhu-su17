package ravotta.carrie.hw5;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RECEIVED", "Broadcast received");
//        final String action = intent.getAction();
//
//        if (REFRESH_ACTION.equals(action)) {
//            // refresh all your widgets
//            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//            ComponentName cn = new ComponentName(context, ScheduleWidgetProvider.class);
//            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.widget_list);
//        }
//        super.onReceive(context, intent);
    }
}
