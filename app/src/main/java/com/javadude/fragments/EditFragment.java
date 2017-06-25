package com.javadude.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by scott on 4/17/2016.
 */
public class EditFragment extends Fragment {
	private long id; // NEW: hold the id of the todoItem being edited
	private EditText first_name;
	private EditText last_name;
	private EditText home_phone;
	private EditText work_phone;
	private EditText mobile_phone;
	private EditText email_address;
	private TodoItem todoItem;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null)
			id = savedInstanceState.getLong("id", -1);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit, container, false);
		// find the fields for the data
		first_name = (EditText) view.findViewById(R.id.first_name);
		last_name = (EditText) view.findViewById(R.id.last_name);
		home_phone = (EditText) view.findViewById(R.id.home_phone);
		work_phone = (EditText) view.findViewById(R.id.work_phone);
		mobile_phone = (EditText) view.findViewById(R.id.mobile_phone);
		email_address = (EditText) view.findViewById(R.id.email_address);

		//todoItem = getIntent().getParcelableExtra("item");

		//first_name.setText(todoItem.getFirst_name());
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("id", id);
	}

	public void setTodoItem(TodoItem item) {
		this.todoItem = item;
		// put the data from the todoItem in the fields
		id = (item.getId()); // NEW: get the id of the todoItem being edited
		first_name.setText(item.getFirst_name());
		last_name.setText(item.getLast_name());
		home_phone.setText(item.getHome_phone());
		work_phone.setText(item.getWork_phone());
		mobile_phone.setText(item.getMobile_phone());
		email_address.setText(item.getEmail_address());
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_done:
				setupData();
				if (onEditFragmentListener != null)
					onEditFragmentListener.onEditFragmentDone(todoItem);
				return true;
			case R.id.action_cancel:
				if (onEditFragmentListener != null)
					onEditFragmentListener.onEditFragmentCancel(todoItem);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	private void setupData() {
		// when the user presses "back", we use that as "save" for now
		//   (we'll replace this with ActionBar buttons later)
		// create a to-do todoItem that we'll return
		todoItem = new TodoItem();
		todoItem.setId(id); // NEW: store the id of the todoItem so we can look it up in the list adapter
		todoItem.setFirst_name(first_name.getText().toString());
		todoItem.setLast_name(last_name.getText().toString());
		todoItem.setLast_name(last_name.getText().toString());
		todoItem.setHome_phone(home_phone.getText().toString());
		todoItem.setWork_phone(work_phone.getText().toString());
		todoItem.setMobile_phone(mobile_phone.getText().toString());
		todoItem.setEmail_address(email_address.getText().toString());
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnEditFragmentListener))
			throw new IllegalStateException("Activities using EditFragment must implement EditFragment.OnEditFragmentListener");
		onEditFragmentListener = (OnEditFragmentListener) context;
	}

	@Override
	public void onDetach() {
		onEditFragmentListener = null;
		super.onDetach();
	}

	private OnEditFragmentListener onEditFragmentListener;

	public interface OnEditFragmentListener {
		void onEditFragmentDone(TodoItem todoItem);
		void onEditFragmentCancel(TodoItem todoItem);
	}
}
