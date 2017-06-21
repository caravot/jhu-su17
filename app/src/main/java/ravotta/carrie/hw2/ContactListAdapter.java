package ravotta.carrie.hw2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

// an adapter for a RecyclerView to manage TodoItem objects
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
	// the real list of TodoItems that we're adapting
	private List<Contact> items;

	// the activity's layout inflater, needed to create instances of the row views
	private LayoutInflater layoutInflater;

	public ContactListAdapter(LayoutInflater layoutInflater, List<Contact> items) {
		this.layoutInflater = layoutInflater;
		this.items = items;
	}

// if we want to have different views for different rows, override getItemViewType
//   and return a number representing the row. The entire set of numbers should be
//   contiguous starting with 0
//	@Override
//	public int getItemViewType(int position) {
//		if (position % 2 == 0)
//			return 0;
//		return 1;
//	}

	// create a ViewHolder that contains a view of the specified type
	@Override
	public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = layoutInflater.inflate(R.layout.contact, parent, false);
		return new ContactViewHolder(view);
	}

	// fill the data into the view for the specified row
	@Override
	public void onBindViewHolder(ContactViewHolder holder, int position) {
		final Contact contact = items.get(position);
//		holder.view.setBackgroundColor(position % 2 == 0 ? Color.LTGRAY : Color.WHITE);
		holder.first_name.setText(contact.getFirst_name());

		// listen to the overall view for clicks - if clicked, notify
		//   the listener so it can navigate
		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				contactListListener.itemSelected(contact);
			}});
	}

	// indicate how many items the recycler view will hold
	@Override
	public int getItemCount() {
		return items.size();
	}

	// react to an item being dragged
	public void onItemMoved(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		// find out which positions the view holders are in
		int fromPosition = viewHolder.getAdapterPosition();
		int toPosition = target.getAdapterPosition();

		// from the "from" view up or down, swapping with all views between it and its destination
		if (fromPosition < toPosition) {
			for(int i = fromPosition; i < toPosition; i++) {
				Collections.swap(items, i, i+1);
			}
		} else {
			for(int i = fromPosition; i > toPosition; i--) {
				Collections.swap(items, i, i-1);
			}
		}

		// tell any listeners (in this case, the RecyclerView) that the item has been moved
		notifyItemMoved(fromPosition, toPosition);
	}

	// react to an item being swiped
	public void onItemDismissed(RecyclerView.ViewHolder viewHolder) {
		// find out where the view that was swiped is
		int position = viewHolder.getAdapterPosition();

		// remove the item in that position from the actual list
		items.remove(position);

		// tell any listeners (in this case, the RecyclerView) that the item has been removed
		notifyItemRemoved(position);
	}

	// a data object that represents a currently-visible row in the recycler view
	// typically this is used to look up the locations of subviews _once_ and hold
	//   onto those view so
	public static class ContactViewHolder extends RecyclerView.ViewHolder {
		private TextView first_name;
		private TextView last_name;
		private View view;

		public ContactViewHolder(View view) {
			super(view);
			this.view = view;
			first_name = (TextView) view.findViewById(R.id.first_name);
			last_name = (TextView) view.findViewById(R.id.last_name);
		}
	}

	// lookup a to-do item by name and replace it in the list
	public void update(Contact contact) {
		for(int i = 0; i < items.size(); i++) {
			Contact contactItem = items.get(i);
			if (contactItem.getFirst_name().equals(contactItem.getFirst_name())) {
				items.set(i, contactItem);
				notifyItemChanged(i);
				break;
			}
		}
	}

	// define a listener interface that we can call to indicate that an item has been clicked
	private ContactListListener contactListListener;

	public void setContactListListener(ContactListListener contactListListener) {
		this.contactListListener = contactListListener;
	}

	public interface ContactListListener {
		void itemSelected(Contact contact);
	}
}
