package ravotta.carrie.hw2;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
	private static final int CONTACTS_LOADER = 42;

	// our model for the RecyclerView
	private ContactCursorAdapter adapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contacts, container, false);
		// get the recycler view
		RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

		// wrap the data in our adapter to use as a model for the recycler view
		adapter = new ContactCursorAdapter(getActivity(), getActivity().getLayoutInflater());
		recyclerView.setAdapter(adapter);

		// layout the items in the recycler view as a vertical list
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// listen to the adapter to find out when an item has been selected
		adapter.setContactsListener(new ContactCursorAdapter.ContactsListener() {
			@Override public void itemSelected(long id) {
				if (onContactsFragmentListener != null)
					onContactsFragmentListener.onContactsFragmentItemSelected(id);
			}});

		// set up support for drag/swipe gestures
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
				new ItemTouchHelper.Callback() {
					// specify which drags/swipes we want to support
					@Override
					public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
						int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
						return makeMovementFlags(0, swipeFlags);
					}

					// if an item is being dragged, tell the adapter
					@Override
					public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
						return true;
					}

					// if an item is being swiped, tell the adapter
					@Override
					public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
						Util.delete(getContext(), viewHolder.getItemId());
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
				if (onContactsFragmentListener != null) {
					onContactsFragmentListener.onContactsFragmentCreateItem();
				}
			}
		});

		return view;
	}

	private OnContactsFragmentListener onContactsFragmentListener;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnContactsFragmentListener)) {
			throw new IllegalStateException("Activities using ContactsFragment must implement ContactsFragment.OnContactsFragmentListener");
		}
		onContactsFragmentListener = (OnContactsFragmentListener) context;
		getActivity().getSupportLoaderManager().initLoader(CONTACTS_LOADER, null, loaderCallbacks);
	}

	@Override
	public void onDetach() {
		onContactsFragmentListener = null;
		super.onDetach();
		getActivity().getSupportLoaderManager().destroyLoader(CONTACTS_LOADER);
	}


	private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			String[] projection = {
					ContactsContentProvider.COLUMN_ID,
					ContactsContentProvider.COLUMN_FIRST_NAME,
					ContactsContentProvider.COLUMN_LAST_NAME,
					ContactsContentProvider.COLUMN_HOME_PHONE,
					ContactsContentProvider.COLUMN_WORK_PHONE,
					ContactsContentProvider.COLUMN_MOBILE_PHONE,
					ContactsContentProvider.COLUMN_EMAIL
			};

			return new CursorLoader(
					getActivity(), ContactsContentProvider.CONTENT_URI, projection, null, null,
					ContactsContentProvider.COLUMN_LAST_NAME + " ASC"
			);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			if (adapter != null)
				adapter.changeCursor(cursor);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			if (adapter != null) {
				adapter.changeCursor(null);
			}
		}
	};

	public interface OnContactsFragmentListener {
		void onContactsFragmentItemSelected(long id);
		void onContactsFragmentCreateItem();
	}
}
