package ravotta.carrie.hw5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static ravotta.carrie.hw5.R.id.textView;

public class Util {

    public static String timestampToSimpleFormat(long value) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a z");
        return sdf.format(value).toString();
    }
    
//    public static long findNextDueItemTime(Context context) {
//        ArrayList<TodoItem> todoItems = findDueTodos(context);
//
//
//    }

    // helper method to find items that are due
    public static long findNextTodoDueTime(Context context) {
        // set up a URI that represents the specific item
        Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "nextdue");

        // set up a projection to show which columns we want to retrieve
        String[] projection = {
                TodoProvider.DUE_TIME
        };

        long nextDueTime = -1;

        // declare a cursor outside the try so we can close it in a finally
        Cursor cursor = null;
        try {
            // ask the content resolver to find the data for the URI
            cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    TodoProvider.STATUS + "!= ? AND " + TodoProvider.DUE_TIME + "<= ?",
                    new String[] {Status.DONE.toString(), Long.toString(System.currentTimeMillis())},
                    TodoProvider.DUE_TIME);

            // if nothing found, return null
            if (cursor == null || !cursor.moveToFirst()) {
                Log.d("findDueTodos", "nothing found");
                return nextDueTime;
            }

            // otherwise return the located item
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                nextDueTime = cursor.getLong(cursor.getColumnIndex(TodoProvider.DUE_TIME));
            }
        } finally {
            // BE SURE TO CLOSE THE CURSOR!!!
            if (cursor != null) {
                cursor.close();
            }
        }
        return nextDueTime;
    }

    // helper method to find items that are due
    public static ArrayList<TodoItem> findDueTodos(Context context) {
        // set up a URI that represents the specific item
        Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "due");

        // declare return
        ArrayList<TodoItem> todoItems = new ArrayList<>();

        // set up a projection to show which columns we want to retrieve
        String[] projection = {
                TodoProvider.ID,
                TodoProvider.NAME,
                TodoProvider.DESCRIPTION,
                TodoProvider.PRIORITY,
                TodoProvider.STATUS,
                TodoProvider.DUE_TIME
        };

        // declare a cursor outside the try so we can close it in a finally
        Cursor cursor = null;
        try {
            // ask the content resolver to find the data for the URI
            cursor = context.getContentResolver().query(
                    uri,
                    projection,
                    TodoProvider.STATUS + "!= ? AND " + TodoProvider.DUE_TIME + "<= ?",
                    new String[] {Status.DONE.toString(), Long.toString(System.currentTimeMillis())},
                    TodoProvider.DUE_TIME);

            // if nothing found, return null
            if (cursor == null || !cursor.moveToFirst()) {
                Log.d("findDueTodos", "nothing found");
                return null;
            }

            // otherwise return the located item
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                todoItems.add(todoItemFromCursor(cursor));
            }
        } finally {
            // BE SURE TO CLOSE THE CURSOR!!!
            if (cursor != null) {
                cursor.close();
            }
        }
        return todoItems;
    }

    // helper method to find an item
    public static TodoItem findTodo(Context context, long id) {
        // set up a URI that represents the specific item
        Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "" + id);

        // set up a projection to show which columns we want to retrieve
        String[] projection = {
                TodoProvider.ID,
                TodoProvider.NAME,
                TodoProvider.DESCRIPTION,
                TodoProvider.PRIORITY,
                TodoProvider.STATUS,
                TodoProvider.DUE_TIME
        };

        // declare a cursor outside the try so we can close it in a finally
        Cursor cursor = null;
        try {
            // ask the content resolver to find the data for the URI
            cursor = context.getContentResolver().query(uri, projection, null, null, null);

            // if nothing found, return null
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }

            // otherwise return the located item
            return todoItemFromCursor(cursor);
        } finally {
            // BE SURE TO CLOSE THE CURSOR!!!
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // helper method to update or insert an item
    public static void updateTodo(Context context, TodoItem todo) {
        // set up the data to store or update
        ContentValues values = new ContentValues();
        values.put(TodoProvider.NAME, todo.name.get());
        values.put(TodoProvider.DESCRIPTION, todo.description.get());
        values.put(TodoProvider.PRIORITY, todo.priority.get());
        values.put(TodoProvider.STATUS, todo.status.get().toString());
        values.put(TodoProvider.DUE_TIME, todo.dueTime.get());

        // if the item didn't yet have an id, insert it and set the id on the object
        if (todo.id.get() == -1) {
            Uri uri = TodoProvider.CONTENT_URI;
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            String idString = insertedUri.getLastPathSegment();
            long id = Long.parseLong(idString);
            todo.id.set(id);

        // otherwise, update the item with that id
        } else {
            // create a URI that represents the item
            Uri uri = Uri.withAppendedPath(TodoProvider.CONTENT_URI, "" + todo.id.get());
            context.getContentResolver().update(uri, values, TodoProvider.ID + "=" + todo.id.get(), null);
        }
    }

    public static TodoItem todoItemFromCursor(Cursor cursor) {
        return new TodoItem(cursor.getLong(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                Status.valueOf(cursor.getString(4)),
                cursor.getLong(5)
        );
    }
}
