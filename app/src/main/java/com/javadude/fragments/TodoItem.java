package com.javadude.fragments;

import android.os.Parcel;
import android.os.Parcelable;

// a parcelable object representing a single to-do item
public class TodoItem implements Parcelable {
	private long id; // NEW: use an id so we can handle updates better
	private String first_name;
	private String last_name;
	private String home_phone;
	private String work_phone;
	private String email_address;

	public TodoItem() {}

	public TodoItem(long id,
					String first_name,
					String last_name,
					String home_phone,
					String work_phone,
					String email_address) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.home_phone = home_phone;
		this.work_phone = work_phone;
		this.email_address = email_address;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getHome_phone() {
		return home_phone;
	}

	public void setHome_phone(String home_phone) {
		this.home_phone = home_phone;
	}

	public String getWork_phone() {
		return work_phone;
	}

	public void setWork_phone(String work_phone) {
		this.work_phone = work_phone;
	}

	public String getEmail_address() {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	// only used by framework code for "special" cases
	// you should always return 0
	@Override
	public int describeContents() {
		return 0;
	}

	// write the data for this instance
	// note that this is significantly more efficient that java serializable, because
	//   we don't need to write metadata describing the class and fields
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(first_name);
		dest.writeString(last_name);
		dest.writeString(home_phone);
		dest.writeString(work_phone);
		dest.writeString(email_address);
	}

	// read the data and create a new instance
	// note that the field MUST be named "CREATOR", all uppercase
	public static Creator<TodoItem> CREATOR = new Creator<TodoItem>() {
		// read the data from the parcel - note that the data read MUST be in the same
		//   order it was written in writeToParcel!
		@Override
		public TodoItem createFromParcel(Parcel source) {
			TodoItem todoItem = new TodoItem();
			todoItem.setId(source.readLong());
			todoItem.setFirst_name(source.readString());
			todoItem.setLast_name(source.readString());
			todoItem.setHome_phone(source.readString());
			todoItem.setWork_phone(source.readString());
			todoItem.setEmail_address(source.readString());
			return todoItem;
		}

		@Override
		public TodoItem[] newArray(int size) {
			return new TodoItem[size];
		}
	};
}
