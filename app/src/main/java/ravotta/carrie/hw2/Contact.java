package ravotta.carrie.hw2;

import android.os.Parcel;
import android.os.Parcelable;

public class Contact implements Parcelable {
    private String first_name;
    private String last_name;
    private String home_phone;
    private String work_phone;
    private String email_address;

    public Contact() {}
    public Contact(String first_name,
                   String last_name,
                   String home_phone,
                   String work_phone,
                   String email_address) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    // write the data for this instance
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(first_name);
        dest.writeString(last_name);
        dest.writeString(home_phone);
        dest.writeString(work_phone);
        dest.writeString(email_address);
    }

    // read the data and create a new instance
    public static Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            Contact contact = new Contact();

            contact.setFirst_name(source.readString());
            contact.setLast_name(source.readString());
            contact.setHome_phone(source.readString());
            contact.setWork_phone(source.readString());
            contact.setEmail_address(source.readString());

            return contact;
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
