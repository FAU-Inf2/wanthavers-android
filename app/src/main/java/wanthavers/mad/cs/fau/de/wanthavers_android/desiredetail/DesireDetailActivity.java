package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class DesireDetailActivity extends AppCompatActivity {

    public static final String EXTRA_DESIRE_ID = "DESIRE_ID";
    private DesireDetailPresenter mDesireDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desiredetail_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.desire);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        long desireId = getIntent().getLongExtra(EXTRA_DESIRE_ID, 4);

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

        DesireRepository fake = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(getApplicationContext()), DesireLocalDataSource.getInstance(context));
        HaverRepository fake_haver = HaverRepository.getInstance(HaverRemoteDataSource.getInstance(getApplicationContext()), HaverLocalDataSource.getInstance(context));

        //create the presenter with Injection of Usecases
        mDesireDetailPresenter = new DesireDetailPresenter(UseCaseHandler.getInstance(),
                desireId,
                desireDetailFragment,
                new AcceptDesire(fake), new GetDesire(fake), new GetHaverList(fake_haver, desireId));

        DesireDetailViewModel desireDetailViewModel =
                new DesireDetailViewModel(getApplicationContext(), mDesireDetailPresenter);

        desireDetailFragment.setViewModel(desireDetailViewModel);

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());

        desireDetailFragment.setDesireLogic(desireLogic);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
