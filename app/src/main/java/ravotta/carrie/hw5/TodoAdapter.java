package ravotta.carrie.hw5;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TodoAdapter extends CursorRecyclerViewAdapter<TodoAdapter.ViewHolder> {
    private Set<Integer> selectedRows = new HashSet<>();
    List<TodoItem> todoItems;

    public interface OnItemClickedListener {
        void onItemClicked(long id);
    }
    private OnItemClickedListener onItemClickedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView descriptionView;
        public TextView priorityView;
        public long id;

        public ViewHolder(View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            descriptionView = (TextView) view.findViewById(R.id.description);
            priorityView = (TextView) view.findViewById(R.id.priority);
        }
    }

    public TodoAdapter(Context context, OnItemClickedListener onItemClickedListener) {
        super(context, null);
        this.onItemClickedListener = onItemClickedListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_card, parent, false);
        view.setClickable(true);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                onItemClickedListener.onItemClicked(vh.id);
            }});
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, Cursor cursor) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.itemView.setSelected(selectedRows.contains(position));
        TodoItem todoItem = Util.todoItemFromCursor(cursor);
        holder.nameView.setText("Carrie");
        holder.descriptionView.setText("Carrie");
        holder.priorityView.setText("5");
        holder.id = 1;
//        holder.nameView.setText(todoItem.getName());
//        holder.descriptionView.setText(todoItem.getDescription());
//        holder.priorityView.setText(String.valueOf(todoItem.getPriority()));
//        holder.id = todoItem.getId();
    }

    public Cursor swapCursor(Cursor newCursor) {
        selectedRows.clear();
        return super.swapCursor(newCursor);
    }
}