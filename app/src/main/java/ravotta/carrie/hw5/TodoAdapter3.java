package ravotta.carrie.hw5;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ravotta.carrie.hw5.databinding.TodoRowBinding;

public class TodoAdapter3 extends CursorRecyclerViewAdapter<TodoAdapter3.ViewHolder> {
    private Set<Integer> selectedRows = new HashSet<>();
    private ObservableList<TodoItem> todoItemObservableList;
    List<TodoItem> todoItems;

    public interface OnItemClickedListener {
        void onItemClicked(long id);
    }
    private OnItemClickedListener onItemClickedListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TodoRowBinding todoRowBinding;

        public ViewHolder(View view) {
            super(view);
            todoRowBinding = DataBindingUtil.bind(view);
        }
    }

    public TodoAdapter3(Context context) {
        super(context, null);
    }

    public TodoAdapter3(Context context, OnItemClickedListener onItemClickedListener) {
        super(context, null);
        this.onItemClickedListener = onItemClickedListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        view.setClickable(true);
        final ViewHolder vh = new ViewHolder(view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View view) {
//                onItemClickedListener.onItemClicked(vh.id);
//            }});
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, Cursor cursor) {
        final TodoItem todoItem = todoItemObservableList.get(position);
        holder.todoRowBinding.setTodoItem(todoItem);
        //This is much important as when we have to bind its method !
        //TODO holder.todoRowBinding.set(this);
        //This is to bind immediately as it schedules binding, so to make force binding!
        holder.todoRowBinding.executePendingBindings();
    }

    public Cursor swapCursor(Cursor newCursor) {
        selectedRows.clear();
        return super.swapCursor(newCursor);
    }
}