package ravotta.carrie.hw5;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import ravotta.carrie.hw5.databinding.ActivityTodoListBinding;

import static ravotta.carrie.hw5.Util.findNextTodoDueTime;

public class TodoListActivity extends AppCompatActivity {
    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODO_LOADER = 1;

    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODOSDUE_LOADER = 2;

    private static final int SNOOZE_ALL = 3;

    private static final int ALL_DONE = 4;

    // listening adaptor
    private TodoAdapter adapter;

    // Data binding for todolist activity
    private ActivityTodoListBinding binding;

    static PendingIntent pendingIntent;

    // Alarms
    private static AlarmManager alarmManager;

    private BroadcastReceiver receiver;

    // time when next item is due
    private static long nextDueTime = -1;

    private ArrayList<TodoItem> todoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String info = getIntent().getStringExtra("info");
        if (info != null) {
            Log.d("infoExtra", info);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodoAdapter(this, new TodoAdapter.OnItemClickedListener() {
            @Override public void onItemClicked(long id) {
                // start activity to edit the item
                Intent intent = new Intent(TodoListActivity.this, EditActivity.class);
                intent.putExtra("itemId", id);
                startActivity(intent);
            }});
        binding.recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start activity to edit the item
                // we're creating a new item; just pass -1 as the id
                Intent intent = new Intent(TodoListActivity.this, EditActivity.class);
                intent.putExtra("itemId", -1L);
                startActivity(intent);
            }
        });

        // create alarm
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // start asynchronous loading of the cursor
        getSupportLoaderManager().initLoader(TODO_LOADER, null, loaderCallbacks);

        // watch for due items
//        getSupportLoaderManager().initLoader(TODOSDUE_LOADER, null, loaderCallbacks);
//
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "here");
        IntentFilter filter = new IntentFilter("ravotta.carrie.hw5.itemsdue");
        receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                int actionId = intent.getIntExtra("actionId", -1);
                Log.d("onResumeInfoCCCC", actionId + ":" + (actionId == SNOOZE_ALL));
                if (actionId == SNOOZE_ALL) {
                    snoozeAllDueItems();
                    cancelAlarm();

                    Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                    context.sendBroadcast(closeIntent);
                } else if (actionId == ALL_DONE) {
                    markAllDueItemsDone();
                    cancelAlarm();
                }
                Log.d("onResume", "onReceive: Broadcast received");
            }};
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        Log.d("onPause", "here");
        //unregisterReceiver(receiver);
//        cancelAlarm();
        super.onPause();
    }

    @Override
    protected void onStart() {
//        Log.d("onStart", "here");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d("onStop", "here");
//        cancelAlarm();
//        unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO ensure this method works as intended
        Log.d("onDestroy","here");
        unregisterReceiver(receiver);
        cancelAlarm();
        super.onDestroy();
    }

    public static void setAlarm(long alarmTime) {
        long dueTime = findNextTodoDueTime(this);
        Log.d("onLoadFinished:Next", Util.timestampToSimpleFormat(dueTime));
        setAlarm(dueTime);
        // TODO change seconds in time elapse
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
    }

    public void cancelAlarm() {
        Log.d("cancelAlarm", "Alarm canceled");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pIntent);
    }

    public void markAllDueItemsDone() {
        ArrayList<TodoItem> todoItems = Util.findDueTodos(this);

        if (todoItems != null) {
            for (int i = 0; i < todoItems.size(); i++) {
                // mark item as done
                todoItems.get(i).status.set(Status.DONE);

                // update the item in the database
                Util.updateTodo(this, todoItems.get(i));
            }
        }
    }

    public void snoozeAllDueItems() {
        ArrayList<TodoItem> todoItems = Util.findDueTodos(this);

        if (todoItems != null) {
            for (int i = 0; i < todoItems.size(); i++) {
                // TODO change time for snooze
                long retryDate = System.currentTimeMillis() + (1 * 60 * 1000);
                todoItems.get(i).dueTime.set(retryDate);

                // update the item in the database
                Util.updateTodo(this, todoItems.get(i));
            }
        }
    }

    // define a loader manager that will asynchronously retrieve data and when finished,
    //   update the list's adapter with the changes
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // when the loader is created, setup the projection to retrieve from the database
        //   and create a cursorloader to request a cursor from a content provider (by URI)
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            Uri uri = TodoProvider.CONTENT_URI;

            // get only due items
            if (loaderId == TODOSDUE_LOADER) {
                uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "nextdue");

                String[] projection = {
                        TodoProvider.ID,
                        TodoProvider.DUE_TIME
                };

                // note: this will register for changes
                return new CursorLoader(
                        TodoListActivity.this,
                        uri,
                        projection,
                        TodoProvider.STATUS + "= ? AND " + TodoProvider.DUE_TIME + "<= ?",
                        new String[] {Status.PENDING.toString(), Long.toString(System.currentTimeMillis())},
                        TodoProvider.DUE_TIME);
            }
            else if (loaderId == TODO_LOADER) {
                String[] projection = {
                        TodoProvider.ID,
                        TodoProvider.NAME,
                        TodoProvider.DESCRIPTION,
                        TodoProvider.PRIORITY,
                        TodoProvider.STATUS,
                        TodoProvider.DUE_TIME
                };

                // note: this will register for changes
                return new CursorLoader(
                        TodoListActivity.this,
                        uri,
                        projection,
                        TodoProvider.STATUS + "!= ?",
                        new String[] {Status.DONE.toString()},
                        TodoProvider.DUE_TIME + " asc, " + TodoProvider.PRIORITY + " asc");
            }
            return null;
        }

        // when the data has been loaded from the content provider, update the list adapter
        //   with the new cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (loader.getId() == TODO_LOADER) {
//                long currentTime = System.currentTimeMillis();

                // find next due item
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    long dueTime = cursor.getLong(cursor.getColumnIndex(TodoProvider.DUE_TIME));
//                    if (dueTime <= currentTime && (nextDueTime == -1 || dueTime <= nextDueTime)) {
//                        Log.d("onLoadFinished", "setting duetime");
//                        nextDueTime = dueTime;
//                    }
//                }

                todoItems = Util.findDueTodos(getApplicationContext());

                // start the alarm if due items
                if (todoItems != null && todoItems.size() > 0) {
                    Log.d("onLoadFinished", "Setting Alarm");
                    // update due time to be in 10 seconds
                    // TODO update to be 10 seconds
                    //long retryDate = System.currentTimeMillis() + (1 * 60 * 1000);
                    setAlarm(System.currentTimeMillis());
                }
                // else get the next item due
                else {
                    long dueTime = findNextTodoDueTime(getApplicationContext());
                    Log.d("onLoadFinished:Next", Util.timestampToSimpleFormat(dueTime));
                    setAlarm(dueTime);
                }

                adapter.swapCursor(cursor); // set the data
            } else if (loader.getId() == TODOSDUE_LOADER) {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    // do what you need with the cursor here
                    //long dueTime = cursor.getLong(cursor.getColumnIndex(TodoProvider.DUE_TIME));
                    //Log.d("dueTimeNewest", dueTime + "");
                }
            }
            // start the alarm
            //setAlarm();
        }

        // if the loader has been reset, kill the cursor in the adapter to remove the data from the list
        @Override
        public void onLoaderReset(Loader<Cursor> cursor) {
            adapter.swapCursor(null); // clear the data
        }
    };

}
