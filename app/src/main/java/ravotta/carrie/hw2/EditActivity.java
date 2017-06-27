package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// Edit activity that uses a Toolbar for actions
public class EditActivity extends AppCompatActivity
	implements EditFragment.OnEditFragmentListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.editFragment);
		long id = getIntent().getLongExtra("item", -1);
		editFragment.setContactId(id);
	}

	@Override
	public void onEditFragmentDone(long id) {
		Intent returnData = new Intent();
		returnData.putExtra("item", id);
		setResult(RESULT_OK, returnData);
		finish();
	}

	@Override
	public void onEditFragmentCancel(long id) {
		finish();
	}

}
