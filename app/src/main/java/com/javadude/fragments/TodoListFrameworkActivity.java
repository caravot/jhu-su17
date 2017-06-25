package com.javadude.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// New version of the TodoListActivity that has a Toolbar
public class TodoListFrameworkActivity extends FragmentFrameworkActivity<TodoListFrameworkActivity.State, TodoListFrameworkActivity.Event, TodoItem>
        implements
        TodoListFragment.OnTodoListFragmentListener,
        AboutFragment.OnAboutFragmentListener,
        ViewFragment.OnViewFragmentListener,
        EditFragment.OnEditFragmentListener {
	public enum State implements FragmentFrameworkActivity.State {
		List, Edit, Exit, View, About
	}
	public enum Event implements FragmentFrameworkActivity.Event {
		ItemSelected, Done, Cancel, NewItem, Back, Edit, About
	}

	private TodoListFragment todoListFragment;
	private EditFragment editFragment;
    private ViewFragment viewFragment;
    private AboutFragment aboutFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_framework);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		todoListFragment = (TodoListFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
        viewFragment = (ViewFragment) getSupportFragmentManager().findFragmentById(R.id.viewFragment);
        aboutFragment = (AboutFragment) getSupportFragmentManager().findFragmentById(R.id.aboutFragment);

		stateMachine()
				.fragmentContainer(R.id.fragmentContainer1)
				.fragmentContainer(R.id.fragmentContainer2)

				.stateType(State.class)
				.initialState(State.List)
				.exitState(State.Exit)
				.backEvent(Event.Back)

                .state(State.List)
                    .fragmentPriority(R.id.todoListFragment, R.id.viewFragment)
                    .on(Event.ItemSelected).goTo(State.View)
                    .on(Event.NewItem).goTo(State.Edit)
                    .on(Event.Back).goTo(State.Exit)

                .state(State.About)
                    .fragmentPriority(R.id.aboutFragment, R.id.todoListFragment)
                    .on(Event.Back).goTo(State.List)

                .state(State.View)
                    .fragmentPriority(R.id.viewFragment, R.id.todoListFragment)
                    .on(Event.About).goTo(State.About)
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
        System.out.println(todoItem);
        System.out.println(todoItem.getFirst_name());
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
        System.out.println("Hello");
        System.out.println("todoItem");
		handleEvent(Event.NewItem, todoItem);
	}

    // View fragment
    @Override
    public void onViewFragmentEdit(TodoItem todoItem) {
        handleEvent(Event.Edit, todoItem);
    }
    public void onViewFragmentAbout() {
        handleEvent(Event.About, null);
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
