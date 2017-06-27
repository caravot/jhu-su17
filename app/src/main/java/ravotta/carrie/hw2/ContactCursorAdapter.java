package ravotta.carrie.hw2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactCursorAdapter extends CursorRecyclerViewAdapter<ContactCursorAdapter.ContactViewHolder> {
	// the activity's layout inflater, needed to create instances of the row views
	private LayoutInflater layoutInflater;

	public ContactCursorAdapter(Context context, LayoutInflater layoutInflater) {
		super(context, null);
		this.layoutInflater = layoutInflater;
	}

	// create a ViewHolder that contains a view of the specified type
	@Override
	public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = layoutInflater.inflate(R.layout.item, parent, false);
		return new ContactViewHolder(view);
	}

	// fill the data into the view for the specified row
	@Override
	public void onBindViewHolder(ContactViewHolder holder, Cursor cursor) {
		final Contact contact = new Contact();
		int idCol = cursor.getColumnIndex(ContactsContentProvider.COLUMN_ID);
		int firstNameCol = cursor.getColumnIndex(ContactsContentProvider.COLUMN_FIRST_NAME);
		int lastNameCol = cursor.getColumnIndex(ContactsContentProvider.COLUMN_LAST_NAME);
		int mobilePhoneCol = cursor.getColumnIndex(ContactsContentProvider.COLUMN_MOBILE_PHONE);

		contact.setId(cursor.getLong(idCol));
		contact.setFirst_name(cursor.getString(firstNameCol));
		contact.setLast_name(cursor.getString(lastNameCol));
		contact.setMobile_phone(cursor.getString(mobilePhoneCol));

		holder.first_name.setText(contact.getFirst_name());
		holder.last_name.setText(contact.getLast_name());
		holder.mobile_phone.setText(contact.getMobile_phone());

		// listen to the overall view for clicks - if clicked, notify
		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				contactsListener.itemSelected(contact.getId());
			}});
	}

	// a data object that represents a currently-visible row in the recycler view
	// typically this is used to look up the locations of subviews _once_ and hold
	//   onto those view so
	public static class ContactViewHolder extends RecyclerView.ViewHolder {
		private TextView first_name;
		private TextView last_name;
		private TextView mobile_phone;
		private View view;

		public ContactViewHolder(View view) {
			super(view);
			this.view = view;
			first_name = (TextView) view.findViewById(R.id.first_name);
			last_name = (TextView) view.findViewById(R.id.last_name);
			mobile_phone = (TextView) view.findViewById(R.id.mobile_phone);
		}
	}

	// define a listener interface that we can call to indicate that an item has been clicked
	private ContactsListener contactsListener;

	public void setContactsListener(ContactsListener contactsListener) {
		this.contactsListener = contactsListener;
	}

	public interface ContactsListener {
		void itemSelected(long id);
	}

}
