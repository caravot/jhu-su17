package ravotta.carrie.hw2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class Main extends AppCompatActivity {
    // request code for the startActivityForResult call
    private static final int REQUEST_EDIT = 42;

    // our model for the RecyclerView
    private ContactListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        // get the recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // create some dummy data
        List<Contact> items = new ArrayList<>();
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        items.add(new Contact("Carrie", "Ravotta", "222", "333", "c.r@gmail.com"));
        System.out.println(items.size());
        // wrap the data in our adapter to use as a model for the recycler view
        adapter = new ContactListAdapter(getLayoutInflater(), items);
        recyclerView.setAdapter(adapter);

        // layout the items in the recycler view as a vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // listen to the adapter to find out when an item has been selected
        adapter.setContactListListener(new ContactListAdapter.ContactListListener() {
            @Override public void itemSelected(Contact contactItem) {
                // if an item is selected, send the item in an intent to the EditActivity
                Intent intent = new Intent(Main.this, EditContact.class);
                intent.putExtra("item", contactItem);
                startActivityForResult(intent, REQUEST_EDIT);
            }});

        // set up support for drag/swipe gestures
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.Callback() {
                    // specify which drags/swipes we want to support
                    @Override
                    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                        return makeMovementFlags(dragFlags, swipeFlags);
                    }

                    // if an item is being dragged, tell the adapter
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        adapter.onItemMoved(viewHolder, target);
                        return true;
                    }

                    // if an item is being swiped, tell the adapter
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        adapter.onItemDismissed(viewHolder);
                    }
                }
        );

        // attach the swipe/gesture support to the recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // when we get an item back from editing, update it in the adapter
        if (requestCode == REQUEST_EDIT) {
            if (resultCode == RESULT_OK) {
                Contact contact = data.getParcelableExtra("item");
                adapter.update(contact);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
