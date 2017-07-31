package ravotta.carrie.hw5;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ravotta.carrie.hw5.databinding.ActivityEditBinding;

// an activty to edit a todo item
public class EditActivity extends AppCompatActivity {
    private TodoItem item;
    private ActivityEditBinding binding;

    // set up the user interface when the activity has been created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit);

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

        binding.editContent.setItem(item);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            // if "done" was pressed, save the data in a new item and return it
            case R.id.done:
                Log.d("ITEM", item.toString());

                // update the item in the database
                Util.updateTodo(this, this.item);

                // flag that we want to remove this activity from the stack and go back
                finish();
                return true;

            // if "cancel" was pressed, just return "canceled" without an item
            case R.id.cancel:
                // flag that we want to remove this activity from the stack and go back
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
