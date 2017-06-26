package com.javadude.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// New version of the TodoListActivity that has a Toolbar
public class TodoListFrameworkActivity
        extends FragmentFrameworkActivity<TodoListFrameworkActivity.State, TodoListFrameworkActivity.Event, TodoItem>
        implements
        TodoListFragment.OnTodoListFragmentListener,
        ViewFragment.OnViewFragmentListener,
        EditFragment.OnEditFragmentListener {
	public enum State implements FragmentFrameworkActivity.State {
		List, Edit, Exit, View
	}
	public enum Event implements FragmentFrameworkActivity.Event {
		ItemSelected, Done, Cancel, NewItem, Back, Edit
	}

	private TodoListFragment todoListFragment;
	private EditFragment editFragment;
    private ViewFragment viewFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_framework);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		todoListFragment = (TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
        viewFragment = (ViewFragment) getSupportFragmentManager().findFragmentById(R.id.viewFragment);

		stateMachine()
				.fragmentContainer(R.id.fragment_container1)
				.fragmentContainer(R.id.fragment_container2)

				.stateType(State.class)
				.initialState(State.List)
				.exitState(State.Exit)
				.backEvent(Event.Back)

                .state(State.List)
                    .fragmentPriority(R.id.todoListFragment, R.id.viewFragment)
                    .on(Event.ItemSelected).goTo(State.View)
                    .on(Event.NewItem).goTo(State.Edit)
                    .on(Event.Back).goTo(State.Exit)

                .state(State.View)
                    .fragmentPriority(R.id.viewFragment, R.id.todoListFragment)
                    .on(Event.ItemSelected).goTo(State.View)
                    .on(Event.Edit).goTo(State.Edit)
                    .on(Event.Back).goTo(State.List)

                .state(State.Edit)
                    .fragmentPriority(R.id.editFragment, R.id.viewFragment)
                    .on(Event.Done).goTo(State.View)
                    .on(Event.Cancel).goTo(State.View)
                    .on(Event.Back).goTo(State.View)

				.state(State.Exit);
	}

	@Override
	protected void onStateChanged(State state, TodoItem todoItem) {
		if (todoItem != null) {
            editFragment.setTodoItem(todoItem);
            viewFragment.setTodoItem(todoItem);
        }
	}

    // Todolist fragment
	@Override
	public void onTodoListFragmentItemSelected(TodoItem todoItem) {
		handleEvent(Event.ItemSelected, todoItem);
	}

	@Override
	public void onTodoListFragmentCreateItem(TodoItem todoItem) {
		handleEvent(Event.NewItem, todoItem);
	}

    // View fragment
    @Override
    public void onViewFragmentEdit(TodoItem todoItem) {
        handleEvent(Event.Edit, todoItem);
    }

	// Edit fragment
	@Override
	public void onEditFragmentDone(TodoItem todoItem) {
		todoListFragment.update(todoItem);
		handleEvent(Event.Done, todoItem);
	}

	@Override
	public void onEditFragmentCancel(TodoItem todoItem) {
		System.out.println(todoItem);
        handleEvent(Event.Cancel, todoItem);
	}
}
