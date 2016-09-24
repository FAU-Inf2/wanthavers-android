package wanthavers.mad.cs.fau.de.wanthavers_android.userprofile;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.desire.DesireRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetDesireList;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class UserProfileActivity extends AppCompatActivity {

    private UserProfilePresenter mUserProfilePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userprofile_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        UserProfileFragment userProfileFragment = (UserProfileFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (userProfileFragment == null) {
            userProfileFragment = UserProfileFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), userProfileFragment, R.id.contentFrame);
        }

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());
        userProfileFragment.setDesireLogic(desireLogic);

        //create repos
        Context context = getApplicationContext();
        checkNotNull(context);

        DesireRepository desireRepository = DesireRepository.getInstance(DesireRemoteDataSource
                .getInstance(getApplicationContext()), DesireLocalDataSource.getInstance(context));

        mUserProfilePresenter = new UserProfilePresenter(userProfileFragment, UseCaseHandler.getInstance(),
                new GetDesireList(desireRepository), new GetDesireList(desireRepository),
                getApplicationContext());

        userProfileFragment.setPresenter(mUserProfilePresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

}
