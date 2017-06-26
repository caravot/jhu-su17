package com.javadude.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.net.Uri;

public class DisplayFragment extends Fragment {
    private long id; // NEW: hold the id of the contact being viewed
    private TextView first_name;
    private TextView last_name;
    private TextView home_phone;
    private TextView work_phone;
    private TextView mobile_phone;
    private TextView email;
    private Contact contact;

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
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        // find the fields for the data
        first_name = (TextView) view.findViewById(R.id.first_name);
        last_name = (TextView) view.findViewById(R.id.last_name);
        home_phone = (TextView) view.findViewById(R.id.home_phone);
        work_phone = (TextView) view.findViewById(R.id.work_phone);
        mobile_phone = (TextView) view.findViewById(R.id.mobile_phone);
        email = (TextView) view.findViewById(R.id.email);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("id", id);
    }

    public void setContact(Contact item) {
        this.contact = item;
        id = (item.getId()); // NEW: get the id of the contact being viewed
        first_name.setText(item.getFirst_name());
        last_name.setText(item.getLast_name());
        home_phone.setText(item.getHome_phone());
        work_phone.setText(item.getWork_phone());
        mobile_phone.setText(item.getMobile_phone());
        email.setText(item.getEmail());
    }

    public void emailContact() {
        String name = contact.getFirst_name() + " " + contact.getLast_name();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", contact.getEmail(), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi " + name + "!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Just wanted to say hi....");
        startActivity(emailIntent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_view, menu);
    }

    public void showAbout() {
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(this, AboutActivity.class);
//                startActivity(intent);
//            }
//        });
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_email:
                emailContact();
                return true;
            case R.id.action_edit:
                if (onDisplayFragmentListener != null)
                    onDisplayFragmentListener.onDisplayFragmentEdit(contact);
                return true;
            case R.id.action_help:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnDisplayFragmentListener))
            throw new IllegalStateException("Activities using DisplayFragment must implement DisplayFragment.OnDisplayFragmentListener");
        onDisplayFragmentListener = (OnDisplayFragmentListener) context;
    }

    @Override
    public void onDetach() {
        onDisplayFragmentListener = null;
        super.onDetach();
    }

    private OnDisplayFragmentListener onDisplayFragmentListener;

    public interface OnDisplayFragmentListener {
        void onDisplayFragmentEdit(Contact contact);
    }
}
