package ravotta.carrie.hw5;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for(int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i], -1);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.javadude.count".equals(intent.getAction())) {
            int count = intent.getIntExtra("count", -1);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            AppWidgetManager instance = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = instance.getAppWidgetIds(componentName);
            for(int i = 0; i < appWidgetIds.length; i++) {
                updateAppWidget(context, instance, appWidgetIds[i], count);
            }
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent();
        intent.setClassName("com.javadude.service", "com.javadude.service.BoundService2");
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.javadude.service", "com.javadude.service.BoundService2");
        context.stopService(intent);
        super.onDisabled(context);
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int count) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        remoteViews.setTextViewText(R.id.text, "count: " + count);

        int color = 0;
        switch(count % 4) {
            case 0: color = Color.RED; break;
            case 1: color = Color.BLUE; break;
            case 2: color = Color.GREEN; break;
            case 3: color = Color.MAGENTA; break;
        }

        if (count % 10 == 0) {
            remoteViews.removeAllViews(R.id.grid);
        } else {
            RemoteViews block = new RemoteViews(context.getPackageName(), R.layout.block);
            block.setInt(R.id.block, "setBackgroundColor", color);
            remoteViews.addView(R.id.grid, block);
        }

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
}
