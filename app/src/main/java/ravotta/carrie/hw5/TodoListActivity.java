package ravotta.carrie.hw5;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
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

import ravotta.carrie.hw5.databinding.ActivityTodoListBinding;

public class TodoListActivity extends AppCompatActivity {
    // define an id for the loader we'll use to manage a cursor and stick its data in the list
    private static final int TODO_LOADER = 1;

    private TodoAdapter3 adapter;

    private ActivityTodoListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodoAdapter3(this, new TodoAdapter3.OnItemClickedListener() {
            @Override public void onItemClicked(long id) {
                Log.d("Carrie", "clicked an item with id: " + id);
                // start activity to edit the item
                // we're creating a new item; just pass -1 as the id
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

        // start asynchronous loading of the cursor
        getSupportLoaderManager().initLoader(TODO_LOADER, null, loaderCallbacks);
    }

    // define a loader manager that will asynchronously retrieve data and when finished,
    //   update the list's adapter with the changes
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        // when the loader is created, setup the projection to retrieve from the database
        //   and create a cursorloader to request a cursor from a content provider (by URI)
        @Override
        public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
            String[] projection = {
                    TodoProvider.ID,
                    TodoProvider.NAME,
                    TodoProvider.DESCRIPTION,
                    TodoProvider.PRIORITY,
                    TodoProvider.STATUS,
                    TodoProvider.DUE
            };

            return new CursorLoader(
                    TodoListActivity.this,
                    TodoProvider.CONTENT_URI, // note: this will register for changes
                    projection,
                    null, null, // groupby, having
                    TodoProvider.NAME + " asc");
        }

        // when the data has been loaded from the content provider, update the list adapter
        //   with the new cursor
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            adapter.swapCursor(cursor); // set the data
        }

        // if the loader has been reset, kill the cursor in the adapter to remove the data from the list
        @Override
        public void onLoaderReset(Loader<Cursor> cursor) {
            adapter.swapCursor(null); // clear the data
        }
    };

}
