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
    private TextView home_phone;
    private TextView work_phone;
    private TextView email_address;
    Contact contact;
    private static final int REQUEST_EDIT = 42;

    // our model for the RecyclerView
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HELLO WORLD THIS IS CARRIE creating a view");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_view);
        setSupportActionBar(toolbar);

        // find the fields for the data
        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);
        home_phone = (TextView) findViewById(R.id.home_phone);
        work_phone = (TextView) findViewById(R.id.work_phone);
        email_address = (TextView) findViewById(R.id.email_address);

        // fetch the parcelable to-do item from the incoming intent
        contact = getIntent().getParcelableExtra("item");

        // put the data from the item in the fields
        first_name.setText(contact.getFirst_name());
        last_name.setText(contact.getLast_name());
        home_phone.setText(contact.getHome_phone());
        work_phone.setText(contact.getWork_phone());
        email_address.setText(contact.getEmail_address());
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
        contact.setHome_phone(home_phone.getText().toString());
        returnData.putExtra("item", contact);

        // set the result to "ok" with the return data
        setResult(RESULT_OK, returnData);

        // WARNING: the super call here MUST appear AFTER setResult
        super.onBackPressed();
    }

    // set up the actions on the Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the tool bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_view, menu);
        return true;
    }

    // handle the actions on the Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_email:
                //saveAndReturnData();
                System.out.println("EMAIL ACTION");
                return true;
            case R.id.action_edit:
                // if an item is selected, send the item in an intent to the EditActivity
                Intent intent = new Intent(ViewContact.this, EditContact.class);
                intent.putExtra("item", contact);
                startActivityForResult(intent, REQUEST_EDIT);

                return true;
            case R.id.action_help:
                System.out.println("HELP ACTION");
                // TODO do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // when we get an item back from editing, update it in the adapter
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) {
                Contact contact = data.getParcelableExtra("item");

                // put the data from the item in the fields
                first_name.setText(contact.getFirst_name());
                last_name.setText(contact.getLast_name());
                home_phone.setText(contact.getHome_phone());
                work_phone.setText(contact.getWork_phone());
                email_address.setText(contact.getEmail_address());

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
