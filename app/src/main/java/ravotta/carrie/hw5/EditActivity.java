package ravotta.carrie.hw5;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ravotta.carrie.hw5.databinding.ActivityEditBinding;

// an activity to edit a todoItem
public class EditActivity extends AppCompatActivity {
    private TodoItem item;
    private ActivityEditBinding binding;

    // set up the user interface when the activity has been created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get the id of the item to edit (or -1 if not set)
        long itemId = getIntent().getLongExtra("itemId", -1L);

        // if there's an id, lookup the item
        if (itemId != -1) {
            item = Util.findTodo(this, itemId);

        // if no id, create a new item
        } else {
            item = new TodoItem();
        }

        binding.setTodoItem(item);
    }

    // set up the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // handle action bar items pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            // if "save" or "snooze" was pressed, save the data in a new item and return it
            case R.id.done: {
                TodoItem todoItem = binding.getTodoItem();
                todoItem.status.set(Status.DONE);

                // update the item in the database
                Util.updateTodo(this, todoItem);

                // flag that we want to remove this activity from the stack and go back
                finish();

                return true;
            }
            // if "save" or "snooze" was pressed, save the data in a new item and return it
            case R.id.snooze:
            case R.id.save: {
                TodoItem todoItem = binding.getTodoItem();

                todoItem.status.set(Status.PENDING);

                // update due time to be in 10 seconds
                // TODO update to be 10 seconds
                long retryDate = System.currentTimeMillis() + (1 * 60 * 1000);
                todoItem.dueTime.set(retryDate);

                // update the item in the database
                Util.updateTodo(this, todoItem);

                // flag that we want to remove this activity from the stack and go back
                finish();

                return true;
            }
            // if "cancel" was pressed, just return "canceled" without an item
            case R.id.cancel: {
                // flag that we want to remove this activity from the stack and go back
                finish();

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
