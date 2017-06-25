package com.javadude.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// Edit activity that uses a Toolbar for actions
public class EditActivity extends AppCompatActivity
	implements EditFragment.OnEditFragmentListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
		TodoItem item = getIntent().getParcelableExtra("item");
		editFragment.setTodoItem(item);
	}

	@Override
	public void onEditFragmentDone(TodoItem todoItem) {
		Intent returnData = new Intent();
		returnData.putExtra("item", todoItem);
		System.out.println("Carrie is: " + todoItem.getFirst_name());
		setResult(RESULT_OK, returnData);
		finish();
	}

	@Override
	public void onEditFragmentCancel(TodoItem todoItem) {
		Intent returnData = new Intent();
		returnData.putExtra("item", todoItem);
		setResult(RESULT_CANCELED, returnData);
		finish();
	}

}
