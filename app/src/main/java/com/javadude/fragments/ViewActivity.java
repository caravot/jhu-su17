package com.javadude.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// View activity that uses a Toolbar for actions
public class ViewActivity extends AppCompatActivity
        implements ViewFragment.OnViewFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("HERE WE ARE carrie");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().findFragmentById(R.id.viewFragment);
        TodoItem item = getIntent().getParcelableExtra("item");
        viewFragment.setTodoItem(item);
    }

    @Override
    public void onViewFragmentEdit(TodoItem todoItem) {
        Intent returnData = new Intent();
        returnData.putExtra("item", todoItem);
        setResult(RESULT_OK, returnData);
        finish();
    }

//    @Override
//    public void onViewFragmentDone(TodoItem todoItem) {
//        Intent returnData = new Intent();
//        returnData.putExtra("item", todoItem);
//        setResult(RESULT_OK, returnData);
//        finish();
//    }

//    @Override
//    public void onViewFragmentCancel(TodoItem todoItem) {
//        finish();
//    }

}
