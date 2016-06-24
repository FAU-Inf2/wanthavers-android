package wanthavers.mad.cs.fau.de.wanthavers_android.desiredetail;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag.FlagLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag.FlagRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.flag.FlagRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.haver.HaverRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.AcceptHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.FlagDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetAcceptedHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetChatForDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesire;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetHaverList;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetHaver;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateDesireStatus;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateHaver;
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

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());

        desireDetailFragment.setDesireLogic(desireLogic);

        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository desireRepository = DesireRepository.getInstance(DesireRemoteDataSource.getInstance(getApplicationContext()), DesireLocalDataSource.getInstance(context));
        HaverRepository haverRepository = HaverRepository.getInstance(HaverRemoteDataSource.getInstance(getApplicationContext()), HaverLocalDataSource.getInstance(context));
        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(getApplicationContext()), UserLocalDataSource.getInstance(context));
        FlagRepository flagRepository = FlagRepository.getInstance(FlagRemoteDataSource.getInstance(getApplicationContext()), FlagLocalDataSource.getInstance(context));

        //create the presenter with Injection of Usecases
        mDesireDetailPresenter = new DesireDetailPresenter(desireLogic, UseCaseHandler.getInstance(),
                desireId, desireDetailFragment,new AcceptHaver(haverRepository), new GetDesire(desireRepository),
                new GetHaverList(haverRepository),new GetUser(userRepository), new SetHaver(haverRepository),
                new GetAcceptedHaver(haverRepository), new GetChatForDesire(desireRepository), new UpdateDesireStatus(desireRepository),
                new FlagDesire(flagRepository));

        DesireDetailViewModel desireDetailViewModel =
                new DesireDetailViewModel(getApplicationContext(), mDesireDetailPresenter);

        desireDetailFragment.setViewModel(desireDetailViewModel);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    /*
    //Never reached code
    @Override
    public boolean onSupportNavigateUp() {
        System.out.println("Reached");
        onBackPressed();
        return true;
    }*/

}
