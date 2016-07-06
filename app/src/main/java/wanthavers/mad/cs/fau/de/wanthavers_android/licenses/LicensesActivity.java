package wanthavers.mad.cs.fau.de.wanthavers_android.licenses;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.media.MediaRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.user.UserRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.GpsLocationTrackerLogic;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.SetImage;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateUser;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;

public class LicensesActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licenses_act);


        //set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        LicensesFragment welcomeFragment = (LicensesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);


        if (welcomeFragment == null) {
            welcomeFragment = LicensesFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    welcomeFragment, R.id.contentFrame);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

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

}
