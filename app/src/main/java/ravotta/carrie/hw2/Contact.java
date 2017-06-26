package ravotta.carrie.hw2;

import android.os.Parcel;
import android.os.Parcelable;

// a parcelable object representing a single to-do item
public class Contact implements Parcelable {
	private long id; // NEW: use an id so we can handle updates better
	private String first_name;
	private String last_name;
	private String home_phone;
	private String work_phone;
	private String mobile_phone;
	private String email;

	public Contact() {}

	public Contact(long id,
				   String first_name,
				   String last_name,
				   String home_phone,
				   String work_phone,
				   String mobile_phone,
				   String email) {
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.home_phone = home_phone;
		this.work_phone = work_phone;
		this.mobile_phone = mobile_phone;
		this.email = email;
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

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		dest.writeString(mobile_phone);
		dest.writeString(email);
	}

	// read the data and create a new instance
	// note that the field MUST be named "CREATOR", all uppercase
	public static Creator<Contact> CREATOR = new Creator<Contact>() {
		// read the data from the parcel - note that the data read MUST be in the same
		//   order it was written in writeToParcel!
		@Override
		public Contact createFromParcel(Parcel source) {
			Contact contact = new Contact();
			contact.setId(source.readLong());
			contact.setFirst_name(source.readString());
			contact.setLast_name(source.readString());
			contact.setHome_phone(source.readString());
			contact.setWork_phone(source.readString());
			contact.setMobile_phone(source.readString());
			contact.setEmail(source.readString());
			return contact;
		}

		@Override
		public Contact[] newArray(int size) {
			return new Contact[size];
		}
	};
}
