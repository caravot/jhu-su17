package ravotta.carrie.hw5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

public class Widget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        ArrayList<TodoItem> todoItems = Util.findDueTodos(context);

        // notify that items are due
        if (todoItems != null) {
            Intent intent = new Intent("ravotta.carrie.hw5.itemsduecount");
            intent.putExtra("count", todoItems.size());
            context.sendBroadcast(intent);

        }
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    // create a pending intent for a specific action
    private static PendingIntent createPending(Context context, int id, String info) {
        Intent intent = new Intent("ravotta.carrie.hw5.action");
        intent.putExtra("actionId", id);

        return PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // update the specific widget
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int count) {
        // pluralize the count in human readable form
        String quantityString = context.getResources().getQuantityString(R.plurals.items, count, count);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.appwidget_text, quantityString);


        PendingIntent mainAction = createPending(context, 0, "View App");
        PendingIntent snoozeAction = createPending(context, 3, "Snooze All");
        PendingIntent addItemAction = createPending(context, 5, "Add Item");

        // Add click options
        views.setOnClickPendingIntent(R.id.appwidget_text, mainAction);
        views.setOnClickPendingIntent(R.id.snooze, snoozeAction);
        views.setOnClickPendingIntent(R.id.add_item, addItemAction);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, 0);
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // ensure our intent is what were looking for
        if ("ravotta.carrie.hw5.itemsduecount".equals(intent.getAction())) {
            int count = intent.getIntExtra("count", -1);

            ComponentName componentName = new ComponentName(context, Widget.class);
            AppWidgetManager instance = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = instance.getAppWidgetIds(componentName);

            // update all widget registered
            for(int i = 0; i < appWidgetIds.length; i++) {
                updateAppWidget(context, instance, appWidgetIds[i], count);
            }
        } else {
            super.onReceive(context, intent);
        }
    }
}