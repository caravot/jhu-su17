package com.javadude.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// View activity that uses a Toolbar for actions
public class DisplayActivity extends AppCompatActivity
        implements DisplayFragment.OnDisplayFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayFragment displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment);
        Contact item = getIntent().getParcelableExtra("item");
        displayFragment.setContact(item);
    }

    @Override
    public void onDisplayFragmentEdit(Contact contact) {
        Intent returnData = new Intent();
        returnData.putExtra("item", contact);
        setResult(RESULT_OK, returnData);
        finish();
    }
}
