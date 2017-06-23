package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditContact extends AppCompatActivity {
    private EditText first_name;
    private EditText last_name;
    private EditText home_phone;
    private EditText work_phone;
    private EditText email_address;
    Contact contact;
    private static final int REQUEST_EDIT = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // find the fields for the data
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);
        home_phone = (EditText) findViewById(R.id.home_phone);
        work_phone = (EditText) findViewById(R.id.work_phone);
        email_address = (EditText) findViewById(R.id.email_address);

        // fetch the parcelable to-do item from the incoming intent
        contact = getIntent().getParcelableExtra("item");

        // put the data from the item in the fields
        first_name.setText(contact.getFirst_name());
        last_name.setText(contact.getLast_name());
        home_phone.setText(contact.getHome_phone());
        work_phone.setText(contact.getWork_phone());
        email_address.setText(contact.getEmail_address());
    }

    public void saveAndReturnData() {
        // create an intent to return
        Intent returnData = new Intent();

        // create a to-do item that we'll return
        Contact contact = new Contact();
        contact.setFirst_name(first_name.getText().toString());
        contact.setLast_name(last_name.getText().toString());
        contact.setLast_name(last_name.getText().toString());
        contact.setHome_phone(home_phone.getText().toString());
        contact.setWork_phone(work_phone.getText().toString());
        contact.setEmail_address(email_address.getText().toString());
        returnData.putExtra("item", contact);

        // set the result to "ok" with the return data
        setResult(RESULT_OK, returnData);
    }

    // set up the actions on the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the tool bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // handle the actions on the Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_done:
                saveAndReturnData();
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
