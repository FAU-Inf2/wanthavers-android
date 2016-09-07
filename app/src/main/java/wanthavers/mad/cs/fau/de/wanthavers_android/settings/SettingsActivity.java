package wanthavers.mad.cs.fau.de.wanthavers_android.settings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.DesireLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.SelectImageLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SendPWResetToken;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

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
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.settings_title));

        SettingsFragment settingsFragment = (SettingsFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (settingsFragment == null) {
            settingsFragment = SettingsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), settingsFragment, R.id.contentFrame);
        }

        DesireLogic desireLogic = new DesireLogic(getApplicationContext());
        settingsFragment.setDesireLogic(desireLogic);

        SelectImageLogic selectImageLogic = new SelectImageLogic(this);
        settingsFragment.setSelectImageLogic(selectImageLogic);

        //create fake task repo
        Context context = getApplicationContext();
        checkNotNull(context);

        UserRepository userRepository = UserRepository.getInstance(UserRemoteDataSource.getInstance(context), UserLocalDataSource.getInstance(context));
        MediaRepository mediaRepository = MediaRepository.getInstance(MediaRemoteDataSource.getInstance(context), MediaLocalDataSource.getInstance(context));

        mSettingsPresenter = new SettingsPresenter(getApplicationContext(), UseCaseHandler.getInstance(), settingsFragment,
                new GetUser(userRepository), new UpdateUser(userRepository), new CreateImage(mediaRepository),
                new SendPWResetToken(userRepository), desireLogic);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
