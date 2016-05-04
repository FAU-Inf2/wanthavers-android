package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

public class DesireDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desiredetail_act);
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
*/

        String desireId = getIntent().getStringExtra(EXTRA_TASK_ID);

        DesireDetailFragment desireDetailFragment = (DesireDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);



        if (desireDetailFragment == null) {
            desireDetailFragment = DesireDetailFragment.newInstance(desireId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireDetailFragment, R.id.contentFrame);
        }

        //new DesireDetailFragment(desireId, Injection.provideTasksRepository(getApplicationContext()),
        //        desireDetailFragment);

        new DesireDetailPresenter("test123",desireDetailFragment);

    }

}
