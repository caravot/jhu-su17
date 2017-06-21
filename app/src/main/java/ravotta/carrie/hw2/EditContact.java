package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class EditContact extends AppCompatActivity {
    private EditText first_name;
    private EditText last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edit);

        // find the fields for the data
        first_name = (EditText) findViewById(R.id.first_name);
        last_name = (EditText) findViewById(R.id.last_name);

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
}
