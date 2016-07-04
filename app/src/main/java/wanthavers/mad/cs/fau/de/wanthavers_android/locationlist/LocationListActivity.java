package wanthavers.mad.cs.fau.de.wanthavers_android.locationlist;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import wanthavers.mad.cs.fau.de.wanthavers_android.R;
import wanthavers.mad.cs.fau.de.wanthavers_android.baseclasses.UseCaseHandler;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationLocalDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRemoteDataSource;
import wanthavers.mad.cs.fau.de.wanthavers_android.data.source.location.LocationRepository;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.CreateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.DeleteLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.GetSavedLocations;
import wanthavers.mad.cs.fau.de.wanthavers_android.domain.usecases.UpdateLocation;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.ActivityUtils;
import wanthavers.mad.cs.fau.de.wanthavers_android.util.WantHaversTextView;

import static com.google.common.base.Preconditions.checkNotNull;

public class LocationListActivity extends AppCompatActivity {

    private LocationListContract.Presenter mLocationListPresenter;

    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);
        setContentView(R.layout.locationlist_act);

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        WantHaversTextView abTitle = (WantHaversTextView) findViewById(R.id.toolbar_title);
        abTitle.setText(getString(R.string.locationlist_title));

        if(getIntent().getStringExtra("calledAct").equals("0")){
            abTitle.setText(getString(R.string.createDesire_title_location_Step));
        }

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        LocationListFragment locationListFragment = (LocationListFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (locationListFragment == null) {
            locationListFragment = LocationListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    locationListFragment, R.id.contentFrame);
        }

        //create fake repo for categories
        Context context = getApplicationContext();
        checkNotNull(context);

        LocationRepository locationRepository = LocationRepository.getInstance(LocationRemoteDataSource.getInstance(getApplicationContext()), LocationLocalDataSource.getInstance(context));

        mLocationListPresenter = new LocationListPresenter(UseCaseHandler.getInstance(), locationListFragment, this, new CreateLocation(locationRepository),
                new GetSavedLocations(locationRepository), new UpdateLocation(locationRepository),
                new DeleteLocation(locationRepository));

        locationListFragment.setPresenter(mLocationListPresenter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        mLocationListPresenter.closeLocationList(null);
        if(getIntent().getStringExtra("calledAct").equals("0")){
           onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(getIntent().getStringExtra("calledAct").equals("0")){
            overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
        }
    }
}
