package ravotta.carrie.hw2;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Util {
	public static Contact findContact(Context context, long id) {
		Uri uri = ContentUris.withAppendedId(ContactsContentProvider.CONTENT_URI, id);

		String[] projection = {
				ContactsContentProvider.COLUMN_ID,
				ContactsContentProvider.COLUMN_FIRST_NAME,
				ContactsContentProvider.COLUMN_LAST_NAME,
				ContactsContentProvider.COLUMN_HOME_PHONE,
				ContactsContentProvider.COLUMN_WORK_PHONE,
				ContactsContentProvider.COLUMN_MOBILE_PHONE,
				ContactsContentProvider.COLUMN_EMAIL
		};

		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(uri, projection, null, null, null);

			if (cursor == null || !cursor.moveToFirst()) {
				return null;
			}

			return new Contact(
					cursor.getLong(0),
					cursor.getString(1), //COLUMN_FIRST_NAME
					cursor.getString(2), //COLUMN_LAST_NAME
					cursor.getString(3), //COLUMN_HOME_PHONE
					cursor.getString(4), //COLUMN_WORK_PHONE
					cursor.getString(5), //COLUMN_MOBILE_PHONE
					cursor.getString(6)  //COLUMN_EMAIL
			);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static void updateContact(Context context, Contact contact) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(ContactsContentProvider.COLUMN_FIRST_NAME, contact.getFirst_name());
		contentValues.put(ContactsContentProvider.COLUMN_LAST_NAME, contact.getLast_name());
		contentValues.put(ContactsContentProvider.COLUMN_HOME_PHONE, contact.getHome_phone());
		contentValues.put(ContactsContentProvider.COLUMN_WORK_PHONE, contact.getWork_phone());
		contentValues.put(ContactsContentProvider.COLUMN_MOBILE_PHONE, contact.getMobile_phone());
		contentValues.put(ContactsContentProvider.COLUMN_EMAIL, contact.getEmail());

		if (contact.getId() != -1) {
			Uri uri = ContentUris.withAppendedId(ContactsContentProvider.CONTENT_URI, contact.getId());
			context.getContentResolver().update(uri, contentValues, null, null);
		} else {
			Uri uri = context.getContentResolver().insert(ContactsContentProvider.CONTENT_URI, contentValues);
			if (uri == null) {
				throw new RuntimeException("No uri returned from insert");
			}
			String stringId = uri.getLastPathSegment();
			long id = Long.parseLong(stringId);
			contact.setId(id);
		}
	}

	public static void delete(Context context, long item) {
		Uri uri = ContentUris.withAppendedId(ContactsContentProvider.CONTENT_URI, item);
		context.getContentResolver().delete(uri, null, null);
	}
}
