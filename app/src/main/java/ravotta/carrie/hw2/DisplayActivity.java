package ravotta.carrie.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

// View activity that uses a Toolbar for actions
public class DisplayActivity extends AppCompatActivity
        implements DisplayFragment.OnDisplayFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DisplayFragment displayFragment = (DisplayFragment) getSupportFragmentManager().findFragmentById(R.id.displayFragment);
        long id = getIntent().getLongExtra("item", -1);
        displayFragment.setContactId(id);
    }

    @Override
    public void onDisplayFragmentEdit(long id) {
        Intent returnData = new Intent();
        returnData.putExtra("item", id);
        setResult(RESULT_OK, returnData);
        finish();
    }
}
