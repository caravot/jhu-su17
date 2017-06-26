package com.javadude.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// New version of the ContactsActivity that has a Toolbar
public class ContactsActivity extends AppCompatActivity
implements ContactsFragment.OnTodoListFragmentListener,
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
		setContentView(R.layout.activity_todo_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);

		sideBySide = (editFragment != null && editFragment.isInLayout());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// when we get an item back from editing, update it in the adapter
		if (requestCode == REQUEST_EDIT) {
			if (resultCode == RESULT_OK) {
				Contact contact = data.getParcelableExtra("item");
				contactsFragment.update(contact);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTodoListFragmentItemSelected(Contact contact) {
		if (sideBySide) {
			editFragment.setContact(contact);
		} else {
			// if an item is selected, send the item in an intent to the EditActivity
			Intent intent = new Intent(ContactsActivity.this, DisplayActivity.class);
			intent.putExtra("item", contact);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onTodoListFragmentCreateItem(Contact contact) {
		if (sideBySide) {
			editFragment.setContact(contact);
		} else {
			// create a new dummy item with a unique ID
			// and send it to the edit activity
			Intent intent = new Intent(ContactsActivity.this, DisplayActivity.class);
			intent.putExtra("item", contact);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onEditFragmentDone(Contact contact) {
		contactsFragment.update(contact);
	}

	@Override
	public void onEditFragmentCancel(Contact contact) {
		// do nothing!
	}
}
