package com.javadude.maps;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

// a parcelable object representing a single to-do item
public class UFOPosition implements Parcelable {
	private long id; // NEW: use an id so we can handle updates better
	private int shipNumber;
	private double lat;
	private double lon;

    public UFOPosition() {}
	public UFOPosition(long id, int shipNumber, double lat, double lon) {
		this.id = id;
		this.shipNumber = shipNumber;
		this.lat = lat;
		this.lon = lon;
	}

    public static Creator<UFOPosition> getCREATOR() {
        return CREATOR;
    }

    public static void setCREATOR(Creator<UFOPosition> CREATOR) {
        UFOPosition.CREATOR = CREATOR;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public int getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
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
		dest.writeInt(shipNumber);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
	}

	// read the data and create a new instance
	// note that the field MUST be named "CREATOR", all uppercase
	public static Creator<UFOPosition> CREATOR = new Creator<UFOPosition>() {
		// read the data from the parcel - note that the data read MUST be in the same
		//   order it was written in writeToParcel!
		@Override
		public UFOPosition createFromParcel(Parcel source) {
            UFOPosition ufoPosition = new UFOPosition();
            ufoPosition.setId(source.readLong());
            ufoPosition.setShipNumber(Integer.parseInt(source.readString()));
            ufoPosition.setLat(Double.parseDouble(source.readString()));
            ufoPosition.setLon(Double.parseDouble(source.readString()));
			return ufoPosition;
		}

		@Override
		public UFOPosition[] newArray(int size) {
			return new UFOPosition[size];
		}
	};
	public String toJsonString() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("shipNumber", shipNumber);
		jsonObject.put("lat", lat);
		jsonObject.put("lon", lon);
		return jsonObject.toString(4);
	}
}
