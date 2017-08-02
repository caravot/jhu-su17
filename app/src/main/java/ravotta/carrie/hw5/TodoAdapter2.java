package ravotta.carrie.hw5;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ravotta.carrie.hw5.databinding.TodoRowBinding;

public class TodoAdapter2 extends RecyclerView.Adapter<TodoAdapter2.BindingHolder> {
    private Set<Integer> selectedRows = new HashSet<>();
	private List<TodoItem> todoItems;
    CursorRecyclerViewAdapter mCursorAdapter;
    public Cursor mCursor;

    public void setmCursorAdapter(CursorRecyclerViewAdapter mCursorAdapter) {
        this.mCursorAdapter = mCursorAdapter;
    }

    public TodoAdapter2(Context context, Cursor cursor) {
        //super(context, null);
        mCursorAdapter = new CursorRecyclerViewAdapter(context, cursor) {
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, Cursor cursor) {
                Log.d("onBindViewHolder", "carrie");
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                Log.d("onCreateViewHolder", "carrie222222");
                return null;
            }
        };
    }

	@Override
	public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
		return new BindingHolder(view);
	}

	@Override
	public void onBindViewHolder(BindingHolder holder, int position) {
        Log.d("ONBINDVIEW", "TEST");
        holder.itemView.setSelected(selectedRows.contains(position));
        //TodoItem todoItem = Util.todoItemFromCursor(cursor);
		TodoItem todoItem = todoItems.get(position);
        Log.d("ONBINDVIEW", todoItem.toString());
		holder.binding.setTodoItem(todoItem);
		holder.binding.executePendingBindings();
	}

	@Override
	public int getItemCount() {
        if (todoItems == null) {
            return 0;
        }
		return todoItems.size();
	}

	public static class BindingHolder extends RecyclerView.ViewHolder {
		private TodoRowBinding binding;
		public BindingHolder(View itemView) {
			super(itemView);
			binding = DataBindingUtil.bind(itemView);
		}
	}

    public Cursor swapCursor(Cursor newCursor) {
        Log.d("swapCursor", "test");
        selectedRows.clear();
        mCursorAdapter.swapCursor(newCursor);
        mCursor = newCursor;
        return mCursor;
    }
}
