package com.javadude.hw2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 4/17/2016.
 */
public class ContactsFragment extends Fragment {
	// NEW: used to generate unique IDs within this run
	//      note that this is a bad idea in general; we should
	//      use a database (and will in a later module)
	private long nextId = 1000;

	// our model for the RecyclerView
	private ContactsAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
		// get the recycler view
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

		// create some dummy data
		List<Contact> items = new ArrayList<>();
		items.add(new Contact(nextId++, "Carrie", "Ravotta", "222-222-2222", "333-333-3333", "444-999-0000", "c.r@gmail.com"));
		items.add(new Contact(nextId++, "Abhinav", "Kumar", "555-555-5555", "111-111-1111", "444-999-0000", "a.k@gmail.com"));
		items.add(new Contact(nextId++, "Andrew", "Doyle", "888-888-8888", "999-999-9999", "444-999-0000", "c.r@hotmail.com"));
		items.add(new Contact(nextId++, "Cindy", "Naughton", "111-111-1111", "222-222-2222", "444-999-0000", "c.n@comcast.net"));
        items.add(new Contact(nextId++, "Carrie", "Ravotta", "222-222-2222", "333-333-3333", "444-999-0000", "c.r@gmail.com"));
        items.add(new Contact(nextId++, "Abhinav", "Kumar", "555-555-5555", "111-111-1111", "444-999-0000", "a.k@gmail.com"));
        items.add(new Contact(nextId++, "Andrew", "Doyle", "888-888-8888", "999-999-9999", "444-999-0000", "c.r@hotmail.com"));
        items.add(new Contact(nextId++, "Cindy", "Naughton", "111-111-1111", "222-222-2222", "444-999-0000", "c.n@comcast.net"));
        items.add(new Contact(nextId++, "Carrie", "Ravotta", "222-222-2222", "333-333-3333", "444-999-0000", "c.r@gmail.com"));
        items.add(new Contact(nextId++, "Abhinav", "Kumar", "555-555-5555", "111-111-1111", "444-999-0000", "a.k@gmail.com"));
        items.add(new Contact(nextId++, "Andrew", "Doyle", "888-888-8888", "999-999-9999", "444-999-0000", "c.r@hotmail.com"));
        items.add(new Contact(nextId++, "Cindy", "Naughton", "111-111-1111", "222-222-2222", "444-999-0000", "c.n@comcast.net"));
        items.add(new Contact(nextId++, "Carrie", "Ravotta", "222-222-2222", "333-333-3333", "444-999-0000", "c.r@gmail.com"));
        items.add(new Contact(nextId++, "Abhinav", "Kumar", "555-555-5555", "111-111-1111", "444-999-0000", "a.k@gmail.com"));
        items.add(new Contact(nextId++, "Andrew", "Doyle", "888-888-8888", "999-999-9999", "444-999-0000", "c.r@hotmail.com"));
        items.add(new Contact(nextId++, "Cindy", "Naughton", "111-111-1111", "222-222-2222", "444-999-0000", "c.n@comcast.net"));
        items.add(new Contact(nextId++, "Carrie", "Ravotta", "222-222-2222", "333-333-3333", "444-999-0000", "c.r@gmail.com"));
        items.add(new Contact(nextId++, "Abhinav", "Kumar", "555-555-5555", "111-111-1111", "444-999-0000", "a.k@gmail.com"));
        items.add(new Contact(nextId++, "Andrew", "Doyle", "888-888-8888", "999-999-9999", "444-999-0000", "c.r@hotmail.com"));
        items.add(new Contact(nextId++, "Cindy", "Naughton", "111-111-1111", "222-222-2222", "444-999-0000", "c.n@comcast.net"));

		// wrap the data in our adapter to use as a model for the recycler view
		adapter = new ContactsAdapter(getActivity().getLayoutInflater(), items);
		recyclerView.setAdapter(adapter);

		// layout the items in the recycler view as a vertical list
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// listen to the adapter to find out when an item has been selected
		adapter.setTodoListListener(new ContactsAdapter.TodoListListener() {
			@Override public void itemSelected(Contact contact) {
				if (onTodoListFragmentListener != null)
					onTodoListFragmentListener.onTodoListFragmentItemSelected(contact);
			}});

		// set up support for drag/swipe gestures
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
				new ItemTouchHelper.Callback() {
					// specify which drags/swipes we want to support
					@Override
					public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
						int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
						int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
						return makeMovementFlags(dragFlags, swipeFlags);
					}

					// if an item is being dragged, tell the adapter
					@Override
					public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
						adapter.onItemMoved(viewHolder, target);
						return true;
					}

					// if an item is being swiped, tell the adapter
					@Override
					public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
						adapter.onItemDismissed(viewHolder);
					}
				}
		);

		// attach the swipe/gesture support to the recycler view
		itemTouchHelper.attachToRecyclerView(recyclerView);

		// NEW: Set up the Floating Action Button to act as "add new item"
		FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
		assert fab != null;
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Contact contact = new Contact(nextId++, "", "", "", "", "", "");
				if (onTodoListFragmentListener != null) {
					onTodoListFragmentListener.onTodoListFragmentCreateItem(contact);
				}
			}
		});

		return view;
	}

	private OnTodoListFragmentListener onTodoListFragmentListener;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnTodoListFragmentListener))
			throw new IllegalStateException("Activities using ContactsFragment must implement ContactsFragment.OnTodoListFragmentListener");
		onTodoListFragmentListener = (OnTodoListFragmentListener) context;
	}

	@Override
	public void onDetach() {
		onTodoListFragmentListener = null;
		super.onDetach();
	}

	public void update(Contact contact) {
		adapter.update(contact);
	}


	public interface OnTodoListFragmentListener {
		void onTodoListFragmentItemSelected(Contact contact);
		void onTodoListFragmentCreateItem(Contact contact);
	}
}
