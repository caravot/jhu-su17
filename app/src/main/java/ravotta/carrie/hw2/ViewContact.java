package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {
    private TextView first_name;
    private TextView last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        // find the fields for the data
        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);

        // fetch the parcelable to-do item from the incoming intent
        Contact contact = getIntent().getParcelableExtra("item");

        // put the data from the item in the fields
        first_name.setText(contact.getFirst_name());
        last_name.setText(contact.getLast_name());
    }

    @Override
    public void onBackPressed() {
        // when the user presses "back", we use that as "save" for now
        //   (we'll replace this with ActionBar buttons later)

        // create an intent to return
        Intent returnData = new Intent();

        // create a to-do item that we'll return
        Contact contact = new Contact();
        contact.setFirst_name(first_name.getText().toString());
        contact.setLast_name(last_name.getText().toString());
        returnData.putExtra("item", contact);

        // set the result to "ok" with the return data
        setResult(RESULT_OK, returnData);

        // WARNING: the super call here MUST appear AFTER setResult
        super.onBackPressed();
    }

    // set up the actions on the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("HELLO WORLD THIS IS CARRIE");
        // Inflate the menu; this adds items to the tool bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_view, menu);
        return true;
    }

    // handle the actions on the Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_done:
                //saveAndReturnData();
                finish();
                return true;
            case R.id.action_cancel:
                finish();
                return true;
            case R.id.action_settings:
                // TODO do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
