package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsActivity extends AppCompatActivity {

    private SettingsPresenter mSettingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_act);

        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.settings_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        SettingsFragment settingsFragment = (SettingsFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
        }

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());
        settingsFragment.setDesireLogic(desireLogic);

        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));

        mSettingsPresenter = new SettingsPresenter(getApplicationContext(), UseCaseHandler.getInstance(), settingsFragment,
                new GetUser(userRepository), new UpdateUser(userRepository));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
