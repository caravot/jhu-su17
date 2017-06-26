package com.javadude.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

// New version of the ContactsActivity that has a Toolbar
public class ContactsFrameworkActivity
        extends FragmentFrameworkActivity<ContactsFrameworkActivity.State, ContactsFrameworkActivity.Event, Contact>
        implements
        ContactsFragment.OnTodoListFragmentListener,
        DisplayFragment.OnDisplayFragmentListener,
        EditFragment.OnEditFragmentListener {
	public enum State implements FragmentFrameworkActivity.State {
		List, Edit, Exit, Display
	}
	public enum Event implements FragmentFrameworkActivity.Event {
		ItemSelected, Done, Cancel, NewItem, Back, Edit
	}

	private ContactsFragment contactsFragment;
	private EditFragment editFragment;
    private DisplayFragment displayFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo_list_framework);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.todoListFragment);
		editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
        displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment);

		stateMachine()
				.fragmentContainer(R.id.fragment_container1)
				.fragmentContainer(R.id.fragment_container2)

				.stateType(State.class)
				.initialState(State.List)
				.exitState(State.Exit)
				.backEvent(Event.Back)

                .state(State.List)
                    .fragmentPriority(R.id.todoListFragment, R.id.displayFragment)
                    .on(Event.ItemSelected).goTo(State.Display)
                    .on(Event.NewItem).goTo(State.Edit)
                    .on(Event.Back).goTo(State.Exit)

                .state(State.Display)
                    .fragmentPriority(R.id.displayFragment, R.id.todoListFragment)
                    .on(Event.ItemSelected).goTo(State.Display)
                    .on(Event.Edit).goTo(State.Edit)
                    .on(Event.Back).goTo(State.List)

                .state(State.Edit)
                    .fragmentPriority(R.id.editFragment, R.id.displayFragment)
                    .on(Event.Done).goTo(State.Display)
                    .on(Event.Cancel).goTo(State.Display)
                    .on(Event.Back).goTo(State.Display)

				.state(State.Exit);
	}

	@Override
	protected void onStateChanged(State state, Contact contact) {
		if (contact != null) {
            editFragment.setContact(contact);
            displayFragment.setContact(contact);
        }
	}

    // Todolist fragment
	@Override
	public void onTodoListFragmentItemSelected(Contact contact) {
		handleEvent(Event.ItemSelected, contact);
	}

	@Override
	public void onTodoListFragmentCreateItem(Contact contact) {
		handleEvent(Event.NewItem, contact);
	}

    // Display fragment
    @Override
    public void onDisplayFragmentEdit(Contact contact) {
        handleEvent(Event.Edit, contact);
    }

	// Edit fragment
	@Override
	public void onEditFragmentDone(Contact contact) {
		contactsFragment.update(contact);
		handleEvent(Event.Done, contact);
	}

	@Override
	public void onEditFragmentCancel(Contact contact) {
		System.out.println(contact);
        handleEvent(Event.Cancel, contact);
	}
}
