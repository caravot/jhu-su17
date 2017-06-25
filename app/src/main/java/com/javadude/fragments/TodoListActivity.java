package com.javadude.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

// New version of the TodoListActivity that has a Toolbar
public class TodoListActivity extends AppCompatActivity
implements TodoListFragment.OnTodoListFragmentListener,
	EditFragment.OnEditFragmentListener {
	// request code for the startActivityForResult call
	private static final int REQUEST_EDIT = 42;
	private static final int REQUEST_VIEW = 55;
	private TodoListFragment todoListFragment;
	private EditFragment editFragment;
	private ViewFragment viewFragment;
	private boolean sideBySide;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		todoListFragment = (TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);

		sideBySide = (editFragment != null && editFragment.isInLayout());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// when we get an item back from editing, update it in the adapter
		if (requestCode == REQUEST_EDIT) {
			if (resultCode == RESULT_OK) {
				TodoItem todoItem = data.getParcelableExtra("item");
				todoListFragment.update(todoItem);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTodoListFragmentItemSelected(TodoItem todoItem) {
		if (sideBySide) {
			editFragment.setTodoItem(todoItem);
		} else {
			// if an item is selected, send the item in an intent to the EditActivity
			Intent intent = new Intent(TodoListActivity.this, ViewActivity.class);
			intent.putExtra("item", todoItem);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onTodoListFragmentCreateItem(TodoItem todoItem) {
		if (sideBySide) {
			editFragment.setTodoItem(todoItem);
		} else {
			// create a new dummy item with a unique ID
			// and send it to the edit activity
			Intent intent = new Intent(TodoListActivity.this, ViewActivity.class);
			intent.putExtra("item", todoItem);
			startActivityForResult(intent, REQUEST_VIEW);
		}
	}

	@Override
	public void onEditFragmentDone(TodoItem todoItem) {
		todoListFragment.update(todoItem);
	}

	@Override
	public void onEditFragmentCancel(TodoItem todoItem) {
		// do nothing!
	}
}
