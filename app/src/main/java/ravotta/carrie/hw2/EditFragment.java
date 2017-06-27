package ravotta.carrie.hw2;

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

public class EditFragment extends Fragment {
	private long id;
	private EditText first_name;
	private EditText last_name;
	private EditText home_phone;
	private EditText work_phone;
	private EditText mobile_phone;
	private EditText email;
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
		View view = inflater.inflate(R.layout.fragment_edit, container, false);
		// find the fields for the data
		first_name = (EditText) view.findViewById(R.id.first_name);
		last_name = (EditText) view.findViewById(R.id.last_name);
		home_phone = (EditText) view.findViewById(R.id.home_phone);
		work_phone = (EditText) view.findViewById(R.id.work_phone);
		mobile_phone = (EditText) view.findViewById(R.id.mobile_phone);
		email = (EditText) view.findViewById(R.id.email);

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong("id", id);
	}

	public void setContactId(long id) {
		this.id = id;
		if (id == -1) {
			first_name.setText("");
			last_name.setText("");
			home_phone.setText("");
			work_phone.setText("");
			mobile_phone.setText("");
			email.setText("");
		} else {
			Contact contact = Util.findContact(getContext(), id);
			if (contact == null) {
				first_name.setText("");
				last_name.setText("");
				home_phone.setText("");
				work_phone.setText("");
				mobile_phone.setText("");
				email.setText("");
				this.id = -1;
			} else {
				first_name.setText(contact.getFirst_name());
				last_name.setText(contact.getLast_name());
				home_phone.setText(contact.getHome_phone());
				work_phone.setText(contact.getWork_phone());
				mobile_phone.setText(contact.getMobile_phone());
				email.setText(contact.getEmail());
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_edit, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_done:
				saveData();
				if (onEditFragmentListener != null)
					onEditFragmentListener.onEditFragmentDone(id);
				return true;
			case R.id.action_cancel:
				if (onEditFragmentListener != null)
					onEditFragmentListener.onEditFragmentCancel(id);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void saveData() {
		contact = new Contact();
		contact.setId(id); // NEW: store the id of the contact so we can look it up in the list adapter
		contact.setFirst_name(first_name.getText().toString());
		contact.setLast_name(last_name.getText().toString());
		contact.setLast_name(last_name.getText().toString());
		contact.setHome_phone(home_phone.getText().toString());
		contact.setWork_phone(work_phone.getText().toString());
		contact.setMobile_phone(mobile_phone.getText().toString());
		contact.setEmail(email.getText().toString());

		Util.updateContact(getContext(), contact);
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (!(context instanceof OnEditFragmentListener)) {
			throw new IllegalStateException("Activities using EditFragment must implement EditFragment.OnEditFragmentListener");
		}
		onEditFragmentListener = (OnEditFragmentListener) context;
	}

	@Override
	public void onDetach() {
		onEditFragmentListener = null;
		super.onDetach();
	}

	private OnEditFragmentListener onEditFragmentListener;

	public interface OnEditFragmentListener {
		void onEditFragmentDone(long id);
		void onEditFragmentCancel(long id);
	}
}
