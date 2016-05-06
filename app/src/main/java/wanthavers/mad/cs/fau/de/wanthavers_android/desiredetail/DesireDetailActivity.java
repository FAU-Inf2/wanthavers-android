package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.TasksRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.local.TasksLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.remote.TasksRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";
    private DesireDetailPresenter mDesireDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desiredetail_act);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.desire);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);


        String desireId = getIntent().getStringExtra(EXTRA_TASK_ID);

        DesireDetailFragment desireDetailFragment = (DesireDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);



        if (desireDetailFragment == null) {
            desireDetailFragment = DesireDetailFragment.newInstance(desireId);

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    desireDetailFragment, R.id.contentFrame);
        }


        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        TasksRepository fake = TasksRepository.getInstance(TasksRemoteDataSource.getInstance(), TasksLocalDataSource.getInstance(context));

        //create the presenter with Injection of Usecases
        mDesireDetailPresenter = new DesireDetailPresenter(UseCaseHandler.getInstance(),
                "test123",
                desireDetailFragment,
                new AcceptDesire(fake), new GetDesire(fake));

    }

}
