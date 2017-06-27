package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// New version of the ContactsActivity that has a Toolbar
public class ContactsActivity extends AppCompatActivity
implements ContactsFragment.OnContactsFragmentListener,
	EditFragment.OnEditFragmentListener {
	// request code for the startActivityForResult call
	private static final int REQUEST_EDIT = 42;
	private static final int REQUEST_VIEW = 55;
	private ContactsFragment contactsFragment;
	private EditFragment editFragment;
	private DisplayFragment displayFragment;
	private boolean sideBySide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);

		sideBySide = (editFragment != null && editFragment.isInLayout());
	}

	@Override
	public void onContactsFragmentItemSelected(long id) {
		if (sideBySide) {
			editFragment.setContactId(id);
		} else {
			// if an item is selected, send the item in an intent to the EditActivity
			Intent intent = new Intent(ContactsActivity.this, DisplayActivity.class);
			intent.putExtra("item", id);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onContactsFragmentCreateItem() {
		if (sideBySide) {
			editFragment.setContactId(-1);
		} else {
			// create a new dummy item with a unique ID
			// and send it to the edit activity
			Intent intent = new Intent(ContactsActivity.this, DisplayActivity.class);
			intent.putExtra("item", -1L);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onEditFragmentDone(long id) {
		// do nothing
	}

	@Override
	public void onEditFragmentCancel(long id) {
		// do nothing
	}
}
