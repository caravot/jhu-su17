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
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.util.Log;
import java.util.Date;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import ravotta.carrie.hw5.databinding.ActivityTodoListBinding;

import static android.R.attr.id;

public class TodoListActivity extends AppCompatActivity {
    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODO_LOADER = 1;

    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODOSDUE_LOADER = 2;

    // listening adaptor
    private TodoAdapter adapter;

    // Data binding for todolist activity
    private ActivityTodoListBinding binding;

    // Alarms
    private AlarmManager alarmManager;

    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        getSupportLoaderManager().initLoader(TODOSDUE_LOADER, null, loaderCallbacks);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d("onResume", "here");
        IntentFilter filter = new IntentFilter("ravotta.carrie.hw5.itemsdue");
        receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context context, Intent intent) {
                Log.d("onResume", "onReceive: Broadcast received");
            }};
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
//        Log.d("onPause", "here");
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
//        Log.d("onStart", "here");
        super.onStart();
    }

    @Override
    protected void onStop() {
//        Log.d("onStop", "here");
        super.onStop();
    }

    public void setAlarm() {
        Log.d("setAlarm", "Alarm set");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO change seconds in time elapse
        //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);
    }

    public void cancelAlarm(View view) {
        Log.d("cancelAlarm", "Alarm canceled");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    // define a loader manager that will asynchronously retrieve data and when finished,
    //   update the list's adapter with the changes
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // when the loader is created, setup the projection to retrieve from the database
        //   and create a cursorloader to request a cursor from a content provider (by URI)

        // uri - the base request from the caller
        // projection - which columns the caller wants to retrieve
        // selection - the "where" clause for the query (without the "where") - usually should have "?" for parameters
        // selectionArgs - the values to use when filling in the "?"
        // sortOrder - the "orderby" clause (without the "orderby")
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            Uri uri = TodoProvider.CONTENT_URI;

            // get only due items
            if (loaderId == TODOSDUE_LOADER) {
                uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "nextdue");
                Date dt = new Date();

                String[] projection = {
                        TodoProvider.ID,
                        TodoProvider.DUE_TIME
                };

                // note: this will register for changes
                return new CursorLoader(
                        TodoListActivity.this,
                        uri,
                        projection,
                        TodoProvider.STATUS + "= ? AND " + TodoProvider.DUE_TIME + " <= ?",
                        new String[] {Status.PENDING.toString(), dt.getTime() + ""},
                        TodoProvider.DUE_TIME);
//                        TodoProvider.DUE_TIME + " ASC LIMIT 1");
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
                        null, null,
                        TodoProvider.DUE_TIME + " asc, " + TodoProvider.PRIORITY + " asc");
            }
            return null;
        }

        // when the data has been loaded from the content provider, update the list adapter
        //   with the new cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (loader.getId() == TODO_LOADER) {
                adapter.swapCursor(cursor); // set the data
            } else if (loader.getId() == TODOSDUE_LOADER) {
                Log.d("onLoadFinished", "Count = " + cursor.getCount());
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    // do what you need with the cursor here
                    long dueTime = cursor.getLong(cursor.getColumnIndex(TodoProvider.DUE_TIME));
                    Log.d("onLoadFinished", Util.timestampToSimpleFormat(dueTime));
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
